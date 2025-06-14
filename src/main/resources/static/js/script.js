document.addEventListener('DOMContentLoaded', function() {
    const questionForm = document.getElementById('questionForm');
    const chatMessages = document.getElementById('chatMessages');
    const loading = document.getElementById('loading');
    const textarea = document.getElementById('question');
    const submitButton = document.getElementById('submitButton');

    // Generate UUID for the conversation
    const conversationId = crypto.randomUUID();

    // Auto-resize textarea
    textarea.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });

    questionForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const question = textarea.value.trim();
        if (!question) return;

        // Add user message to chat
        addMessage(question, 'user');
        
        // Clear input
        textarea.value = '';
        textarea.style.height = 'auto';
        
        // Show loading
        loading.classList.add('active');
        
        // Submit form with conversation ID
        const formData = new FormData();
        formData.append('question', question);
        formData.append('conversationId', conversationId);
        
        fetch('/ask', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(html => {
            // Create a temporary div to parse the HTML response
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;
            
            // Get the response content from the Thymeleaf template
            const responseContent = tempDiv.querySelector('.message.ai div');
            if (responseContent) {
                addMessage(responseContent.innerHTML, 'ai');
                
                // Check if the form is disabled in the response
                const isDisabled = tempDiv.querySelector('#submitButton').disabled;
                if (isDisabled) {
                    submitButton.disabled = true;
                    textarea.disabled = true;
                    textarea.value = 'Chat terminated';
                    submitButton.style.backgroundColor = '#9ca3af';
                    submitButton.style.cursor = 'not-allowed';
                    textarea.style.backgroundColor = '#f3f4f6';
                    textarea.style.cursor = 'not-allowed';
                }
            } else {
                console.error('Could not find response content in the HTML');
                addMessage('Sorry, there was an error processing your request.', 'ai');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            addMessage('Sorry, there was an error processing your request.', 'ai');
        })
        .finally(() => {
            loading.classList.remove('active');
        });
    });

    function addMessage(content, type) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type}`;
        messageDiv.innerHTML = content;
        chatMessages.appendChild(messageDiv);
        
        // Scroll to bottom
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
}); 
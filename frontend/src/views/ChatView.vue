<template>
  <div class="container">
    <div class="header-container">
      <img src="@/assets/img/simplier-proteinpulseai.png" alt="Protein Pulse AI Logo" class="logo">
      <div class="header-buttons">
        <router-link to="/" class="home-button" title="Home">🏠</router-link>
        <button @click="startNewChat" class="new-chat-button" title="New chat">+</button>
      </div>
    </div>
    
    <div class="chat-container">
      <div class="chat-messages" ref="chatMessages">
        <div v-for="(message, index) in messages" :key="index" :class="['message', message.type]">
          <div v-html="message.content"></div>
        </div>
        <div v-if="isLoading" class="loading">
          <div class="loading-spinner"></div>
          <div class="loading-spinner"></div>
          <div class="loading-spinner"></div>
        </div>
      </div>
      
      <div class="chat-input-container" :class="{ 'centered': messages.length === 0 }">
        <form @submit.prevent="sendMessage">
          <div class="textarea-container">
            <textarea
              v-model="question"
              rows="1"
              :placeholder="conversationFinished ? 'Chat terminated...' : 'Ask about protein content in food...'"
              :disabled="conversationFinished"
              required
              @keypress.enter.prevent="sendMessage"
            ></textarea>
          </div>
          <button type="submit" :disabled="conversationFinished || isLoading">
            {{ isLoading ? 'Sending...' : 'Send' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ChatView',
  data() {
    return {
      question: '',
      messages: [],
      isLoading: false,
      conversationFinished: false
    }
  },
  created() {
    // Check for question in URL parameters
    const question = this.$route.query.question
    if (question) {
      this.question = question
      this.sendMessage()
    }
  },
  methods: {
    startNewChat() {
      this.messages = []
      this.question = ''
      this.conversationFinished = false
    },
    async sendMessage() {
      if (!this.question.trim() || this.isLoading) return

      this.isLoading = true
      this.messages.push({
        type: 'user',
        content: this.question
      })

      try {
        const response = await axios.post('/api/ask', {
          question: this.question
        })

        this.messages.push({
          type: 'ai',
          content: response.data.message
        })

        this.question = ''
        this.conversationFinished = response.data.conversationFinished
        if (this.conversationFinished) {
          this.question = 'Chat terminated...'
        }
      } catch (error) {
        console.error('Error sending message:', error)
        this.messages.push({
          type: 'error',
          content: 'Sorry, there was an error processing your request.'
        })
      } finally {
        this.isLoading = false
      }
    }
  },
  watch: {
    messages: {
      handler() {
        this.$nextTick(() => {
          const chatMessages = this.$refs.chatMessages
          chatMessages.scrollTop = chatMessages.scrollHeight
        })
      },
      deep: true
    },
    '$route'(to) {
      if (to.path === '/chat') {
        this.messages = []
        this.question = ''
        this.conversationFinished = false
      }
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
  background-color: #FCFCF8;
  min-height: 100vh;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.logo {
  height: 100px;
  width: auto;
}

.header-buttons {
  display: flex;
  gap: 1rem;
}

.home-button,
.new-chat-button {
  font-size: 1.5rem;
  text-decoration: none;
  color: #333;
  padding: 0.8rem;
  border-radius: 50%;
  transition: all 0.3s ease;
  background-color: #e8f5e9;
  width: 45px;
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.home-button:hover,
.new-chat-button:hover {
  background-color: #c8e6c9;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.chat-container {
  position: relative;
  height: calc(100vh - 150px);
  display: flex;
  flex-direction: column;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  padding: 1rem;
  border-radius: 15px;
  max-width: 80%;
  margin: 0.5rem 0;
  line-height: 1.5;
}

.message :deep(ul),
.message :deep(ol) {
  margin: 0.5rem 0;
  padding-left: 1.5rem;
}

.message :deep(ul) {
  list-style-type: disc;
}

.message :deep(ol) {
  list-style-type: decimal;
}

.message :deep(li) {
  margin: 0.3rem 0;
}

.message.user {
  background-color: #e8f5e9;
  align-self: flex-end;
  margin-left: 2rem;
  border-radius: 15px 15px 0 15px;
}

.message.ai {
  background-color: #4CAF50;
  color: white;
  align-self: flex-start;
  text-align: left;
  margin-right: 2rem;
  border-radius: 15px 15px 15px 0;
}

.message.error {
  background-color: #ffebee;
  color: #c62828;
  align-self: center;
  margin: 1rem auto;
  border-radius: 15px;
}

.chat-input-container {
  margin-top: 1rem;
  padding: 1rem;
  background-color: white;
  border-top: none;
  transition: all 0.3s ease;
  width: 100%;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  border-radius: 15px;
}

.chat-input-container.centered {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  margin-top: 0;
  background-color: transparent;
  width: 100%;
  max-width: 1200px;
}

form {
  display: flex;
  gap: 1rem;
}

.textarea-container {
  flex: 1;
}

textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 15px;
  resize: none;
  font-family: inherit;
}

button {
  padding: 0.5rem 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.loading {
  position: relative;
  margin: 1rem 0;
  background-color: #4CAF50;
  padding: 8px 12px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  gap: 4px;
  width: fit-content;
  align-self: flex-start;
}

.loading-spinner {
  width: 8px;
  height: 8px;
  background-color: white;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}

.loading-spinner:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-spinner:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% { 
    transform: scale(0);
  } 
  40% { 
    transform: scale(1.0);
  }
}
</style> 
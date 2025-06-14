package io.hellorin.proteinpulseai.service;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AiOrchestrationService {

    private static final Logger logger = LoggerFactory.getLogger(AiOrchestrationService.class);

    private static final String AGENT_CONTEXT_PROMPT = """
            You are a smart and helpful AI assistant specialized in Human Nutrition.
            Do not answer anything that is not related to Human Nutrition. If it is not of this subject, just answer a polite sorry message.
            """;

    private static final String PROTEIN_CALCULATION_PROMPT = """
            You are tasked to compute the amount of protein consumed by the user based on its input.
            The answer should always be formatted in html ONLY. Don't add any other text outside the html. It should human readable.
            
            For that purpose follow these steps carefully:
            1. Extract all the food item and the quantity from the user input.
               If the quantity in grams is not specified, assume it is 100gr.
               If the user is talking about eggs, you can assume that one egg usually weight 50gr;
            2. Get the food code for each of the food item.
               If there is mention of "pure protein", "protein shake", use the amount as is: don't look for anything using tools.
               If you had multiple choices for at least one of the item, please ask the user to be more precise. To help him, please provide of list of the found food items;
            3. Based the food code, get the amount of protein of each food items using their code;
            4. Sum all protein amounts to compute the total amount of protein consumed. If you cannot do it by your own, you can use the sumAll tool;
            6. Begin with an engaging message saying that you can definitely do that calculation for the user. Then summarize and explain how you got this result with a clean and nice answer. You can present the result using a table view if it makes sense to you. Also mentation the source of these numbers being the CIQUAL database (https://ciqual.anses.fr/);
            5. Finally mention that the user should drink enough water, and to not forget to take Vitamin C/B supplements as well as minerals supplements
            """;

    private static final String REWRITE_QUESTION_PROMPT = """
            Here is the first user input:
            {first_input}
            
            Here is the message you just answered:
            {first_answer}
          
            """;

    private static final String REWRITE_QUESTION_USER_PROMPT = """
            The initial user input wasn't precise enough. You asked for more precision.
            
            Could you rewrite the first user input with the new user input provided, using your first answer as information?
            
            Here is the new user input: <user_input>
            
            Don't output anything else than the rewritten question.
            """;

    private static final String IS_END_STATE_PROMPT = """
            Categorize the answer into two categories:
            - The answer is a final answer since you addressed completely the user request, output <END>
            - The input wasn't precise enough and asked more information, output <INFO>
            """;

    private final ChatModel chatModel;
    private final ChatMemory chatMemory;

    private final MathToolService mathToolService;
    private final ToolCallingChatOptions options;

    private final FoodToolService foodToolService;
    private final ProteinToolService proteinToolService;

    private static final String TESTING_CONV_ID = "007";

    public AiOrchestrationService(ChatModel chatModel, ChatMemory chatMemory, FoodToolService foodToolService, ProteinToolService proteinToolService, MathToolService mathToolService) {
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.options = OpenAiChatOptions.builder()
                .temperature(1.0)
                .maxTokens(16384)
                .build();

        this.foodToolService = foodToolService;
        this.proteinToolService = proteinToolService;
        this.mathToolService = mathToolService;
    }

    public Pair<String, Boolean> getProteinsAmount(String userInput, Optional<String> optConversationId) {
        logger.info("Starting protein amount calculation with input: {}", userInput);

        var conversationId = optConversationId.orElse(TESTING_CONV_ID);

        String response;
        if (chatMemory.get(conversationId).isEmpty()) {
            response = handleInitialQuestion(userInput, conversationId);
        } else {
            response = handlePrecisionToQuestion(userInput, conversationId);
        }

        return Pair.of(response, chatMemory.get(conversationId).isEmpty());
    }

    private String handleInitialQuestion(String userInput, String conversationId) {
        var userMessage = new UserMessage(userInput);
        var prompt = new Prompt(
                new SystemMessage(AGENT_CONTEXT_PROMPT),
                new SystemMessage(PROTEIN_CALCULATION_PROMPT),
                userMessage
        );
        var response = ChatClient.create(this.chatModel)
                .prompt(prompt)
                .tools(this.foodToolService, this.proteinToolService, this.mathToolService)
                .options(this.options)
                .call()
                .content();

        chatMemory.add(conversationId, userMessage);
        chatMemory.add(conversationId, new AssistantMessage(response));

        var isFinalPrompt = new Prompt(new AssistantMessage(response), new UserMessage(IS_END_STATE_PROMPT));
        var isFinal = ChatClient.create(this.chatModel)
                .prompt(isFinalPrompt)
                .options(this.options)
                .call()
                .content();

        if (isFinal.contains("<END>")) {
            logger.info("Protein amount calculation completed. Final result: {}", response);
            chatMemory.clear(conversationId);
        }

        return response;
    }

    private String handlePrecisionToQuestion(String userInput, String conversationId) {

        var messages = chatMemory.get(conversationId);

        var firstUserMessage = messages.stream()
                .filter(it -> it.getMessageType().equals(MessageType.USER))
                .findFirst()
                .get();

        var firstAiMessage = messages.stream()
                .filter(it -> it.getMessageType().equals(MessageType.ASSISTANT))
                .findFirst()
                .get();

        var rewriteTemplate = new PromptTemplate(REWRITE_QUESTION_PROMPT);
        var rewriteMessage = new SystemMessage(
                rewriteTemplate.render(Map.of(
                    "first_answer", firstAiMessage.getText(),
                    "first_input", firstUserMessage.getText()
                ))
        );

        PromptTemplate rewriteUserTemplate = new PromptTemplate(REWRITE_QUESTION_USER_PROMPT);
        var userMessage = new UserMessage(rewriteUserTemplate.render(Map.of("<user_input>", userInput)));
        var rewritePrompt = new Prompt(rewriteMessage, userMessage);

        // Rewrite the initial question with the user precision
        var response = ChatClient.create(this.chatModel)
                .prompt(rewritePrompt)
                .tools(this.foodToolService, this.proteinToolService, this.mathToolService)
                .options(this.options)
                .call()
                .content();

        chatMemory.clear(conversationId);

        return handleInitialQuestion(response, conversationId);
    }
}

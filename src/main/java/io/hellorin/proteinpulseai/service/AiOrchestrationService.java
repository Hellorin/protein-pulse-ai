package io.hellorin.proteinpulseai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiOrchestrationService {

    private static final Logger logger = LoggerFactory.getLogger(AiOrchestrationService.class);

    private static final String AGENT_CONTEXT_PROMPT = """
            You are a smart and helpful AI assistant specialized in Human Nutrition.
            Do not answer anything that is not related to Human Nutrition. If it is not of this subject, just answer a polite sorry message.
            """;

    private static final String PROTEIN_CALCULATION_PROMPT = """
            You are tasked to compute the amount of protein consumed by the user based on its input.
            
            For that purpose follow these steps carefully:
            1. Extract all the food item and the quantity from the user input. If the quantity in grams is not specified, assume it is 100gr;
            2. Get the food code for each of the food item. If you find more than one result for a given food item, just keep the most relevant one. If you cannot find it once, please try again at least two more time;
            3. Based the food code, get the amount of protein of each food items using their code;
            4. Sum all protein amounts to compute the total amount of protein consumed. If you cannot do it by your own, you can use the sumAll tool;
            6. Begin with an engaging message saying that you can definitely do that calculation for the user. Then summarize and explain how you got this result with a clean and nice answer. You can present the result using a table view if it makes sense to you. Also mentation the source of these numbers being the CIQUAL database (https://ciqual.anses.fr/);
            5. Finally mention that the user should drink enough water, and to not forget to take Vitamin C/B supplements as well as minerals supplements
            """;

    private final ChatModel chatModel;
    private final MathService mathService;
    private final ToolCallingChatOptions options;

    private final FoodService foodService;
    private final ProteinService proteinService;

    public AiOrchestrationService(ChatModel chatModel, FoodService foodService, ProteinService proteinService, MathService mathService) {
        this.chatModel = chatModel;
        this.options = OpenAiChatOptions.builder()
                .temperature(1.0)
                .maxTokens(16384)
                .build();

        this.foodService = foodService;
        this.proteinService = proteinService;
        this.mathService = mathService;
    }

    public String getProteinsAmount(String userInput) {
        logger.info("Starting protein amount calculation with input: {}", userInput);

        SystemMessage generalSystemPromptMessage = new SystemMessage(AGENT_CONTEXT_PROMPT);
        SystemMessage systemPromptMessage = new SystemMessage(PROTEIN_CALCULATION_PROMPT);

        UserMessage userMessage = new UserMessage(userInput);

        Prompt prompt = new Prompt(generalSystemPromptMessage, systemPromptMessage, userMessage);

        userInput = ChatClient.create(this.chatModel)
                .prompt(prompt)
                .tools(this.foodService, this.proteinService, this.mathService)
                .options(this.options)
                .call()
                .content();

        logger.info("Protein amount calculation completed. Final result: {}", userInput);
        return userInput;
    }
}

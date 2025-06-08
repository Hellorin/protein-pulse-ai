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

    private static final String[] DEFAULT_SYSTEM_PROMPTS = {

            // Step 1
            """
					Extract all the food item and the quantity from the user input.
					If the quantity in grams is not specified, assume it is 100gr.
					Output Format: It should be in a comma-seperated list.
					Example: How much protein did I consume if I had chicken and egg ?
					Expected Result: chicken|100gr,egg|100gr
					
					Do not add anything else that the food items.
					""",
            // Step 2
            """
					Get the food code for each of the food item by using the findFoodItemsByNames tool.
					If you find more than one result for a given food item, just keep the most relevant one.
					Output Format: It should output the code and the name on each item like 'code1|food1|quantity1'
					Example: <outpu>chicken|100gr,egg|100gr
					Expected Result:
					    chicken|code1|100gr
					    egg|code2|100gr
					""",
            // Step 3
            """
					For each line couple of food|code, use the findProteinQuantityInFoodItem tool to get the amount of protein of the food using the code.
					Output Format: It should output the code and the name on each item like 'proteinFood1,proteinFood2,proteinFood3'
					Example:
					    chicken|code1|100gr
					    egg|code2|100gr
					Expected Result:
					    chicken|21,egg|12
					""",
            // Step 4
            """
					Use the addAll tool to compute the sum of amount after the | in the coma-seperated list of the input: that is the total amount of protein the user consumed.
					For example: chicken|17.3,egg|12.7, it is 17.3 + 12.7. The amount should be 30.0
					From that calculated amount and the input, write the beautiful answer with explanation of the contribution of each food item.
					Also mentation the source of these numbers being the CIQUAL database (https://ciqual.anses.fr/).
					Finally mention that the user should drink enough water, and to not forget to take Vitamin C/B supplements as well as minerals supplements
					"""
    };

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

    public String getProteinsAmount(String userInput) throws InterruptedException {
        logger.info("Starting protein amount calculation with input: {}", userInput);
        
        SystemMessage generalSystemPromptMessage = new SystemMessage(AGENT_CONTEXT_PROMPT);

        for (int i = 0; i < DEFAULT_SYSTEM_PROMPTS.length; i++) {
            logger.info("Processing step {} with input: {}", i + 1, userInput);
            
            SystemMessage systemPromptMessage = new SystemMessage(DEFAULT_SYSTEM_PROMPTS[i]);
            UserMessage userMessage = new UserMessage(userInput);

            Prompt prompt = new Prompt(generalSystemPromptMessage, systemPromptMessage, userMessage);

            userInput = ChatClient.create(this.chatModel)
                    .prompt(prompt)
                    .tools(this.foodService, this.proteinService, this.mathService)
                    .options(this.options)
                    .call()
                    .content();

            logger.info("Step {} completed. Output: {}", i + 1, userInput);
        }
        
        logger.info("Protein amount calculation completed. Final result: {}", userInput);
        return userInput;
    }
}

package io.hellorin.proteinpulseai.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hellorin.proteinpulseai.model.FoodItem;
import io.hellorin.proteinpulseai.model.FoodItemList;
import io.hellorin.proteinpulseai.repository.NutritionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling food-related operations and interactions with AI models.
 * This service provides functionality to search and process food items using AI-powered text processing.
 * It integrates with a nutrition repository and uses Spring AI for natural language processing tasks.
 */
@Service
public class FoodToolService {

    private static final Logger logger = LoggerFactory.getLogger(FoodToolService.class);
    private final ChatModel chatModel;
    private final OpenAiChatOptions options;
    private final NutritionRepository nutritionRepository;

    public FoodToolService(ChatModel chatModel, NutritionRepository nutritionRepository) {
        this.chatModel = chatModel;
        this.nutritionRepository = nutritionRepository;

        this.options = OpenAiChatOptions.builder()
                .temperature(1.0)
                .maxTokens(16384)
                .build();
    }

    /**
     * Searches for food items by their names using AI-powered text processing.
     * This method uses an AI model to match food names against a database of food items
     * and returns their corresponding codes.
     *
     * @param name A string containing the name of food item to search for
     * @return Optional containing a string of matched food items and their codes, or empty if no matches found
     * @throws JsonProcessingException if there's an error processing JSON data
     */
    @Tool(name = "findFoodItemsByName", description = "Find food items by their name")
    public Optional<String> findFoodItemsByName(String name) throws JsonProcessingException {
        logger.info("Tool findFoodItemsByName called with parameters - names: {}", name);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        FoodItemList foodItemList = new FoodItemList();
        FoodItem foodItem1 = new FoodItem("code1", "chicken");
        FoodItem foodItem2 = new FoodItem("code2", "tofu");
        foodItemList.setFoodItems(List.of(foodItem1, foodItem2));

        String structure = objectMapper.writeValueAsString(
                foodItemList.getFoodItems().stream().map(it -> it.getAlimCode() + "|" + it.getAlimNomEng()).toList()
        );

        String data = objectMapper.writeValueAsString(
                this.nutritionRepository.getFoodItems().stream().map(it -> it.getAlimCode() + "|" + it.getAlimNomEng()).toList()
        );

        String systemPrompt = """
            You are a helpful AI assistant specialized in finding a object from a list of objects in json format based on the attribute 'alim_nom_eng'.
            
            The structure is as follow:
            <structure>
            
            If I search for 'chickens', It would return the code 'code1' for example.
            """.replace("<structure>", structure);
        SystemMessage systemPromptMessage = new SystemMessage(systemPrompt);
        UserMessage userMessage = new UserMessage("""
                Here is the data to find the food code from:
                <data>
                
                And the food we are looking for are '<food>'
                
                As an additional rule, if the food is 'egg', get the code for 'egg' ONLY. IGNORE the other codes.
                
                Please don't explain your reasoning and only output the code like 'food name:code1' on each line.
                
                If you find multiple return only the most relevant one.
                If you didn't find anything, please return '<NO_ANSWER>'.""".replace("<data>", data)
                .replace("<food>", name)
        );

        String content = ChatClient.create(this.chatModel)
                .prompt(new Prompt(systemPromptMessage, userMessage))
                .options(this.options)
                .call()
                .content();

        if (content.contains("<NO_ANSWER>")) {
            return Optional.empty();
        } else {
            return Optional.of(content);
        }
    }
}

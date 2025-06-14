package io.hellorin.proteinpulseai.service;

import io.hellorin.proteinpulseai.repository.NutritionRepository;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class responsible for handling protein-related calculations and operations.
 * This service integrates with AI capabilities through Spring AI and manages protein
 * quantity calculations for food items.
 */
@Service
public class ProteinToolService {

    private final NutritionRepository nutritionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProteinToolService.class);

    public ProteinToolService(NutritionRepository nutritionRepository) {
        this.nutritionRepository = nutritionRepository;
    }

    /**
     * Calculates the quantity of pure protein in a given food item based on its weight.
     * This method is exposed as a tool for AI interactions.
     *
     * @param foodItemCode The unique code identifying the food item
     * @param quantityInGrams The weight of the food item in grams
     * @return A string representation of the protein quantity with 2 decimal places, or "0.00" if the food item is not found
     */
    @Tool(name = "findProteinQuantityInFoodItem", description = "Find the quantity of pure protein a in food item")
    public String findProteinQuantityInFoodItem(String foodItemCode, Integer quantityInGrams) {
        logger.info("Tool findProteinQuantityInFoodItem called with parameters - foodItemCode: {}, quantityInGrams: {}", foodItemCode, quantityInGrams);
        if (nutritionRepository.getProteinCompositions().containsKey(foodItemCode)) {
            String amount = nutritionRepository.getProteinCompositions().get(foodItemCode).getTeneur().replace(",", ".").trim().strip();
            double result = (quantityInGrams.doubleValue() / 100.0) * Double.parseDouble(amount);
            return String.format("%.2f", result);
        } else {
            return "0.00";
        }
    }

}
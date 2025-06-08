package io.hellorin.proteinpulseai.service;

import io.hellorin.proteinpulseai.model.Composition;
import io.hellorin.proteinpulseai.repository.NutritionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProteinServiceTest {

    @Mock
    private ChatModel chatModel;

    @Mock
    private NutritionRepository nutritionRepository;

    private ProteinService proteinService;

    @BeforeEach
    void setUp() throws Exception {
        proteinService = new ProteinService(chatModel, nutritionRepository);
    }

    @Test
    void findProteinQuantityInFoodItem_WhenFoodItemExists_ShouldCalculateCorrectProteinQuantity() {
        // Arrange
        String foodItemCode = "TEST001";
        Integer quantityInGrams = 200;
        Map<String, Composition> proteinCompositions = new HashMap<>();
        Composition composition = new Composition();
        composition.setTeneur("25,5");
        proteinCompositions.put(foodItemCode, composition);
        
        when(nutritionRepository.getProteinCompositions()).thenReturn(proteinCompositions);

        // Act
        String result = proteinService.findProteinQuantityInFoodItem(foodItemCode, quantityInGrams);

        // Assert
        assertThat(result).isEqualTo("51.00");
    }

    @Test
    void findProteinQuantityInFoodItem_WhenFoodItemDoesNotExist_ShouldReturnZero() {
        // Arrange
        String foodItemCode = "NONEXISTENT";
        Integer quantityInGrams = 200;
        Map<String, Composition> proteinCompositions = new HashMap<>();
        
        when(nutritionRepository.getProteinCompositions()).thenReturn(proteinCompositions);

        // Act
        String result = proteinService.findProteinQuantityInFoodItem(foodItemCode, quantityInGrams);

        // Assert
        assertThat(result).isEqualTo("0.00");
    }

    @Test
    void findProteinQuantityInFoodItem_WhenQuantityIsZero_ShouldReturnZero() {
        // Arrange
        String foodItemCode = "TEST001";
        Integer quantityInGrams = 0;
        Map<String, Composition> proteinCompositions = new HashMap<>();
        Composition composition = new Composition();
        composition.setTeneur("25,5");
        proteinCompositions.put(foodItemCode, composition);
        
        when(nutritionRepository.getProteinCompositions()).thenReturn(proteinCompositions);

        // Act
        String result = proteinService.findProteinQuantityInFoodItem(foodItemCode, quantityInGrams);

        // Assert
        assertThat(result).isEqualTo("0.00");
    }
} 
package io.hellorin.proteinpulseai.repository;

import io.hellorin.proteinpulseai.model.Composition;
import io.hellorin.proteinpulseai.model.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NutritionRepositoryTest {

    private NutritionRepository nutritionRepository;

    @BeforeEach
    void setUp() {
        nutritionRepository = new NutritionRepository();
    }

    @Test
    void testLoadAndGetFoodItems() {
        // Arrange
        List<FoodItem> foodItems = Arrays.asList(
            new FoodItem("1", "Apple"),
            new FoodItem("2", "Banana")
        );

        // Act
        nutritionRepository.loadFood(foodItems);
        List<FoodItem> result = nutritionRepository.getFoodItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getAlimNomEng());
        assertEquals("Banana", result.get(1).getAlimNomEng());
    }

    @Test
    void testGetProteinCompositions() {
        // Arrange
        List<Composition> compositions = Arrays.asList(
            new Composition("1", "25000", "10.5"), // Protein composition
            new Composition("2", "25000", "15.0"), // Protein composition
            new Composition("3", "26000", "5.0")   // Non-protein composition
        );
        nutritionRepository.loadCompositions(compositions);

        // Act
        Map<String, Composition> proteinCompositions = nutritionRepository.getProteinCompositions();

        // Assert
        assertNotNull(proteinCompositions);
        assertEquals(2, proteinCompositions.size());
        assertTrue(proteinCompositions.containsKey("1"));
        assertTrue(proteinCompositions.containsKey("2"));
        assertFalse(proteinCompositions.containsKey("3"));
        assertEquals("10.5", proteinCompositions.get("1").getTeneur());
        assertEquals("15.0", proteinCompositions.get("2").getTeneur());
    }

} 
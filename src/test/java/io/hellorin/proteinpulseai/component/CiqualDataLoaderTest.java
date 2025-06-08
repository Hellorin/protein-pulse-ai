package io.hellorin.proteinpulseai.component;

import io.hellorin.proteinpulseai.model.Composition;
import io.hellorin.proteinpulseai.model.FoodItem;
import io.hellorin.proteinpulseai.repository.NutritionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CiqualDataLoaderTest {

    @Mock
    private NutritionRepository nutritionRepository;

    private CiqualDataLoader ciqualDataLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ciqualDataLoader = new CiqualDataLoader(nutritionRepository);
    }

    @Test
    void testLoadFoodDatabase() throws Exception {
        // Execute
        List<FoodItem> foodItems = ciqualDataLoader.loadFoodDatabase();

        // Verify
        assertNotNull(foodItems);
        assertFalse(foodItems.isEmpty());
        // Verify that the XML file exists and can be read
        assertTrue(new ClassPathResource("alim_2020_07_07.xml").exists());
    }

    @Test
    void testLoadCompositionTable() throws Exception {
        // Execute
        List<Composition> compositions = ciqualDataLoader.loadCompositionTable();

        // Verify
        assertNotNull(compositions);
        assertFalse(compositions.isEmpty());
        // Verify that the XML file exists and can be read
        assertTrue(new ClassPathResource("compo_2020_07_07.xml").exists());
    }

    @Test
    void testRun() throws Exception {
        // Execute
        ciqualDataLoader.run();

        // Verify that both repository methods were called
        verify(nutritionRepository, times(1)).loadFood(anyList());
        verify(nutritionRepository, times(1)).loadCompositions(anyList());
    }
} 
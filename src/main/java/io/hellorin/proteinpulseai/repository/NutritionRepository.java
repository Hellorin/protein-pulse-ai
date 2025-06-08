package io.hellorin.proteinpulseai.repository;

import io.hellorin.proteinpulseai.model.Composition;
import io.hellorin.proteinpulseai.model.FoodItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository class responsible for managing and providing access to nutrition-related data.
 * This repository maintains collections of food items and their nutritional compositions,
 * particularly focusing on protein content information.
 */
@Repository
public class NutritionRepository {
    private List<FoodItem> foodItems;
    private List<Composition> compositions;

    public void loadFood(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public void loadCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    /**
     * Retrieves a map of protein compositions for food items.
     * Filters compositions to only include those with the protein constituent code (ciqual code 25000).
     * In case of duplicate entries, the first occurrence is retained.
     *
     * @return Map where keys are food item codes and values are their protein compositions
     */
    public Map<String, Composition> getProteinCompositions() {
        return compositions.stream()
                .filter(comp -> "25000".equals(comp.getConstCode().strip().trim()))
                .collect(Collectors.toMap(
                        comp -> comp.getAlimCode().trim(),
                        comp -> comp,
                        (existing, replacement) -> existing // In case of duplicates, keep the first one
                ));
    }
}

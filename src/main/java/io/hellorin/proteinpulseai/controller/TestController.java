package io.hellorin.proteinpulseai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hellorin.proteinpulseai.service.FoodService;
import io.hellorin.proteinpulseai.service.ProteinService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller for testing food-related functionality.
 * This controller is only active when the 'test' profile is enabled.
 * It provides endpoints for testing food search and protein-related operations.
 */
@RestController
@RequestMapping("/api/test/food")
@Profile("test")
public class TestController {

    private final ProteinService proteinService;

    private final FoodService foodService;

    public TestController(FoodService foodService, ProteinService proteinService) {
        this.proteinService = proteinService;
        this.foodService = foodService;
    }

    @GetMapping("/search")
    public Optional<String> findFoodItemsByName(@RequestParam String names) throws JsonProcessingException {
        return foodService.findFoodItemsByNames(names);
    }
} 
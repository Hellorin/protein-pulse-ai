package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProteinCalculationControllerTest {

    @Mock
    private AiOrchestrationService aiOrchestrationService;

    private ProteinCalculationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProteinCalculationController(aiOrchestrationService);
    }

    @Test
    void getProteinsAmount_ShouldReturnExpectedResult() throws InterruptedException {
        // Arrange
        String userInput = "I need 100g of protein";
        String expectedResponse = "You should consume 100g of protein";
        when(aiOrchestrationService.getProteinsAmount(userInput)).thenReturn(expectedResponse);

        // Act
        String result = controller.getProteinsAmount(userInput);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void getProteinsAmount_ShouldHandleEmptyInput() throws InterruptedException {
        // Arrange
        String userInput = "";
        String expectedResponse = "Please provide valid input";
        when(aiOrchestrationService.getProteinsAmount(userInput)).thenReturn(expectedResponse);

        // Act
        String result = controller.getProteinsAmount(userInput);

        // Assert
        assertEquals(expectedResponse, result);
    }
} 
package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protein")
public class ProteinCalculationController {

    private final AiOrchestrationService aiOrchestrationService;

    public ProteinCalculationController(AiOrchestrationService aiOrchestrationService) {
        this.aiOrchestrationService = aiOrchestrationService;
    }

    @PostMapping("/amount")
    public String getProteinsAmount(@RequestBody String userInput) throws InterruptedException {
        return aiOrchestrationService.getProteinsAmount(userInput);
    }
} 
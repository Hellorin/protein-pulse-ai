package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/amount", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getProteinsAmount(@RequestBody String userInput) throws InterruptedException {
        return aiOrchestrationService.getProteinsAmount(userInput);
    }
} 
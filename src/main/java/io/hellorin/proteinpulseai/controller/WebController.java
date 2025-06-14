package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class WebController {

    private final AiOrchestrationService aiOrchestrationService;

    public WebController(AiOrchestrationService aiOrchestrationService) {
        this.aiOrchestrationService = aiOrchestrationService;
    }

    @PostMapping("/api/ask")
    @ResponseBody
    public ChatResponse askQuestionApi(@RequestBody QuestionRequest request) {
        var response = aiOrchestrationService.getProteinsAmount(request.question(), Optional.ofNullable(request.conversationId()));
        return new ChatResponse(response.getLeft(), response.getRight());
    }

    public record QuestionRequest(String question, String conversationId) {}
    
    public record ChatResponse(String message, boolean conversationFinished) {}
} 
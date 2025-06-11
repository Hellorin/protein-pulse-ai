package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final AiOrchestrationService aiOrchestrationService;

    public WebController(AiOrchestrationService aiOrchestrationService) {
        this.aiOrchestrationService = aiOrchestrationService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam String question, Model model) {
        String response = aiOrchestrationService.getProteinsAmount(question);
        model.addAttribute("question", question);
        model.addAttribute("response", response);
        return "index";
    }
} 
package io.hellorin.proteinpulseai.controller;

import io.hellorin.proteinpulseai.service.AiOrchestrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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

    @GetMapping("/chat")
    public String chat(@RequestParam(required = false) String question, Model model) {
        if (question != null) {
            model.addAttribute("question", question);
        }
        model.addAttribute("conversationFinished", false);
        return "chat";
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam String question, @RequestParam(required = false) String conversationId, Model model) {
        var response = aiOrchestrationService.getProteinsAmount(question, Optional.ofNullable(conversationId));
        boolean isFinished = response.getRight();
        model.addAttribute("question", question);
        model.addAttribute("response", response.getLeft());
        model.addAttribute("conversationFinished", isFinished);
        return "chat";
    }
} 
package com.fastprep.site.controller;

import com.fastprep.site.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/test-gemini")
    public String testGemini(@RequestParam(defaultValue = "Explain AI in simple terms") String prompt) {
        return geminiService.generateContent(prompt);
    }
}
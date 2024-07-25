package com.ai.gpt.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class ChatControllerExample {

    private final OpenAiChatModel chatModel;

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        final String demoResponse = "This is a demo response";
        return Map.of("generation", demoResponse);
//        return Map.of("generation", chatModel.call(message));
    }
}

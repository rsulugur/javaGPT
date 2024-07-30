package com.ai.gpt.service;


import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OpenAIService {

    private final OpenAiChatModel chatModel;

    public String sendMessage(String productName) {
        try {
            return chatModel.call("Generate 10 Word Description for Product" + productName);
//            return "Described " + productName;
        } catch (Exception ex) {
            return null;
        }
    }
}

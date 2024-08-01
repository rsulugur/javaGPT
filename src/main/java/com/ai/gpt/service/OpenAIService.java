package com.ai.gpt.service;


import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OpenAIService {

    private final OpenAiChatModel chatModel;

    public String sendMessage(String query) {
        try {
            return chatModel.call("Generate 10 Word Description for my Query" + query);
//            return "Described " + query;
        } catch (Exception ex) {
            return "Described " + query;
        }
    }
}

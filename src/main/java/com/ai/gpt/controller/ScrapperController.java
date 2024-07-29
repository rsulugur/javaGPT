package com.ai.gpt.controller;

import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductRequest;
import com.ai.gpt.service.ScrapperService;
import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ScrapperController {

    private final OpenAiChatModel chatModel;
    private final ScrapperService service;

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        final String demoResponse = "This is a demo response";
        return Map.of("generation", demoResponse);
//        return Map.of("generation", chatModel.call(message));
    }

    @PostMapping("/v1/fetch/product")
    public List<Product> fetchProducts(@RequestBody ProductRequest request) {
        return service.scrapProductDetails(
                request.productName()
        );
    }

    @GetMapping("/v1/searchProduct")
    public List<Product> searchH2Database() {
        return List.of();
    }
}

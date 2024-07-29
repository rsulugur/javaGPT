package com.ai.gpt.controller;

import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductRequest;
import com.ai.gpt.service.ScrapperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Product Controller", description = "API for managing products")
public class ScrapperController {

    private final OpenAiChatModel chatModel;
    private final ScrapperService service;

    @GetMapping("/ai/generate")
    @Tag(name = "ChatGPT Controller", description = "API for invoking OpenAI with Custom Search Parameters")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        // return Map.of("generation", chatModel.call(message));
        return "This is a demo response";

    }

    @PostMapping("/v1/fetch/product")
    @Tag(name = "Product Scrapping Controller", description = "API for initiating and scraping products")
    public List<Product> fetchProducts(@RequestBody ProductRequest request) {
        return service.scrapProductDetails(request.productName());
    }
}

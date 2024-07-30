package com.ai.gpt.controller;

import com.ai.gpt.model.ErrorObject;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductRequest;
import com.ai.gpt.model.ProductResponse;
import com.ai.gpt.service.OpenAIService;
import com.ai.gpt.service.ScrapperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class ScrapperController {

    private final ScrapperService service;
    private OpenAIService openAIService;

    @GetMapping("/ai/generate")
    @Tag(name = "ChatGPT Controller", description = "API for invoking OpenAI with Custom Search Parameters")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//         return openAIService.sendMessage(message);
        return "This is a demo response";
    }

    // , @RequestParam(required = false, defaultValue = "asc") String sortByPrice
    @PostMapping("/v1/fetch/product")
    @Tag(name = "Product Scrapping Controller", description = "API for initiating and scraping products")
    public ProductResponse fetchProducts(@RequestBody ProductRequest request) {
        final List<ErrorObject> errorObjects = new ArrayList<>();
        final List<Product> products = service.scrapProductDetails(request, errorObjects);
        final ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();
        return productResponse.productList(products).errors(errorObjects).build();
    }
}

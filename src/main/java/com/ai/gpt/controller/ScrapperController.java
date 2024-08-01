package com.ai.gpt.controller;

import com.ai.gpt.model.Audit;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.SearchRequest;
import com.ai.gpt.service.AsyncScrapperService;
import com.ai.gpt.service.AuditService;
import com.ai.gpt.service.OpenAIService;
import com.ai.gpt.service.SyncScrapperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ScrapperController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperController.class);

    private final SyncScrapperService syncService;
    private final AsyncScrapperService asyncService;
    private OpenAIService openAIService;
    private AuditService auditService;

    @GetMapping("/ai/generate")
    @Tag(name = "ChatGPT Controller", description = "API for invoking OpenAI with Custom Search Parameters")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return openAIService.sendMessage(message);
    }

    @GetMapping("/v1/fetch/products")
    @Tag(name = "Product Scrapping Controller", description = "API for initiating and scraping products")
    public List<Product> getProducts(@RequestParam(required = true) String searchKey) {
        LOGGER.info("initiating web scrapping for {}", searchKey);
        return syncService.scrapProductDetails(searchKey);
    }

    @PostMapping("/v2/fetch/products")
    @Tag(name = "Product Scrapping Controller", description = "API for initiating and scraping products")
    public List<Product> postProducts(@RequestBody SearchRequest request) {
        LOGGER.info("initiating web scrapping for {}", request.getQuery());
        return asyncService.scrapProductDetails(request);
    }

    @GetMapping("/v1/recent")
    @Tag(name = "Product Scrapping Controller", description = "API for initiating and scraping products")
    public List<Audit> fetchRecentProducts() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);
        LOGGER.info("Serving RecentProducts GET Request");
        return auditService.findAll(pageable);
    }
}

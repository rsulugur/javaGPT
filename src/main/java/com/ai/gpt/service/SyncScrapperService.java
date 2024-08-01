package com.ai.gpt.service;

import com.ai.gpt.mapper.ProductMapper;
import com.ai.gpt.model.Audit;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductScrappers;
import com.ai.gpt.scrapper.AmazonScrapper;
import com.ai.gpt.scrapper.BestBuyScrapper;
import com.ai.gpt.scrapper.EbayScrapper;
import com.ai.gpt.scrapper.Scrapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class SyncScrapperService {
    private static final Logger logger = LoggerFactory.getLogger(SyncScrapperService.class);

    private final AuditService auditService;
    private final OpenAIService openAIService;

    public List<Product> scrapProductDetails(final String searchKey) {
        final List<Scrapper> availableScrappers = List.of(new AmazonScrapper(), new EbayScrapper());
        final Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        final Map<String, Integer> consideredCount = new HashMap<>();
        final Long startTime = System.currentTimeMillis();
        final List<Product> products = new ArrayList<>();

        for (Scrapper scrapper : availableScrappers) {
            List<Product> productBatch = scrapper.crawl(searchKey);
            logger.info("fetched {} products from {}", productBatch.size(), scrapper.getClass().getSimpleName());
            for (Product product : productBatch) {
                product.setDesc(openAIService.sendMessage(product.getName()));
                products.add(product);
            }
            consideredCount.put(scrapper.getClass().getSimpleName(), productBatch.size());
        }
        final Long endTime = System.currentTimeMillis();
        products.sort(comparator);
        final Audit auditReport = new Audit();
        auditReport.setSearchQuery(searchKey);
        auditReport.setTimeTaken(((endTime - startTime) / 1000));
        auditReport.setResults(consideredCount);
        auditService.save(auditReport);
        return products;
    }
}

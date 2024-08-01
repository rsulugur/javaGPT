package com.ai.gpt.service;

import com.ai.gpt.model.Audit;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.SearchRequest;
import com.ai.gpt.scrapper.AmazonScrapper;
import com.ai.gpt.scrapper.BestBuyScrapper;
import com.ai.gpt.scrapper.EbayScrapper;
import com.ai.gpt.scrapper.Scrapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service
@AllArgsConstructor
public class AsyncScrapperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncScrapperService.class);

    public List<Product> scrapProductDetails(final SearchRequest request) {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        final List<Product> scrappedProducts = new ArrayList<>();
        final List<Scrapper> availableScrappers = List.of(new AmazonScrapper(), new EbayScrapper());

        for (Scrapper scrapper : availableScrappers) {
            Future<List<Product>> futureTask = executorService.submit(() -> scrapper.crawl(request.getQuery()));
            try {
                List<Product> products = futureTask.get();
                scrappedProducts.addAll(products);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("unable to scrap {} {}", scrapper.getClass(), e.getMessage());
            }
        }

        return scrappedProducts;
    }
}

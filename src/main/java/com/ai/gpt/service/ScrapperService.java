package com.ai.gpt.service;

import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductScrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrapperService {
    private final ProductScrappers scrappers;
    private final OpenAIService openAIService;

    public List<Product> scrapProductDetails(final String productName, String sortByPrice) {
        final Comparator<Product> comparator = Comparator.comparing(Product::getProductPrice);

        return this.scrappers
                .scrapperList()
                .stream()
                .flatMap(scrapper -> scrapper.scrap(productName))
                .peek(product -> product.setProductDescription(openAIService.sendMessage(product.getProductName())))
                .sorted(sortByPrice.equalsIgnoreCase("asc") ? comparator : comparator.reversed())
                .toList();

    }
}

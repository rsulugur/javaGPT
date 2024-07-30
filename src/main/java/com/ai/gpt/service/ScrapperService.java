package com.ai.gpt.service;

import com.ai.gpt.mapper.ProductMapper;
import com.ai.gpt.model.ErrorObject;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductRequest;
import com.ai.gpt.model.ProductScrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ScrapperService {
    private final ProductScrappers scrappers;
    private final ProductMapper productMapper;
    private final OpenAIService openAIService;

    public List<Product> scrapProductDetails(final ProductRequest productRequest, List<ErrorObject> errorObjects) {
        final Comparator<Product> comparator = Comparator.comparing(Product::getProductPrice);
        final String sortCondition = Objects.requireNonNullElse(productRequest.sortByPrice(), "asc");
        try {
            return this.scrappers
                    .scrapperList()
                    .stream()
                    .flatMap(scrapper -> scrapper.scrap(productRequest.productName(), errorObjects))
                    .peek(product -> {
                        product.setProductDescription(openAIService.sendMessage(product.getProductName()));
                        productMapper.insertProduct(product);
                    })
                    .sorted(sortCondition.equalsIgnoreCase("asc") ? comparator : comparator.reversed())
                    .toList();
        } catch (Exception ex) {
            errorObjects.add(new ErrorObject(ex.getMessage()));
            return List.of();
        }
    }
}

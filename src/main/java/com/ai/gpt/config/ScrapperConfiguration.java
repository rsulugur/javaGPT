package com.ai.gpt.config;

import com.ai.gpt.model.ProductScrappers;
import com.ai.gpt.scrapper.AmazonScrapper;
import com.ai.gpt.scrapper.EbayScrapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class ScrapperConfiguration {
    private final AmazonScrapper amazonScrapper;
    private final EbayScrapper ebayScrapper;

    @Bean
    public ProductScrappers getAvailableScrappers() {
        return new ProductScrappers(
                List.of(ebayScrapper, amazonScrapper)
        );
    }
}

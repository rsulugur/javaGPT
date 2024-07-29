package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;

import java.util.List;

public interface Scrapper {
    List<Product> scrap(final String itemName);
}

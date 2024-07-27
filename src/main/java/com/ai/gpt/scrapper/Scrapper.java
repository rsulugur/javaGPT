package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;

import java.util.List;

public interface Scrapper {
    public List<Product> scrap(final String itemName);
}

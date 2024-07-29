package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;

import java.util.stream.Stream;

public interface Scrapper {
    Stream<Product> scrap(final String itemName);
}

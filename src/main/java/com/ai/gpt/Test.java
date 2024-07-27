package com.ai.gpt;

import com.ai.gpt.model.Product;
import com.ai.gpt.scrapper.AmazonScrapper;
import com.ai.gpt.scrapper.Scrapper;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        Scrapper scrapper = new AmazonScrapper();

        List<Product> scrap = scrapper.scrap("Iphone 15 Pro max");

        for (Product product : scrap) {
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Product Description: " + product.getProductDescription());
            System.out.println("Product Price: " + product.getProductPrice());
            System.out.println("-----------------------------------------------");
        }
    }
}

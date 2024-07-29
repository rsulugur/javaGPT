package com.ai.gpt.service;

import com.ai.gpt.mapper.ProductMapper;
import com.ai.gpt.model.Product;
import com.ai.gpt.model.ProductScrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ScrapperService {
    private final ProductScrappers scrappers;
    private final ProductMapper productMapper;

    // Method to call the API and return the response as a String
    private static String callApi(HttpClient client, String productName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/ai/generate?message=" + productName))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response is successful
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Failed to fetch data for ID: " + productName + " Status Code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Product> scrapProductDetails(final String productName, String sortByPrice) {
        final List<Product> productList = new ArrayList<>();
        this.scrappers.scrapperList().forEach(scrapper -> {
            final List<Product> tempProducts = scrapper.scrap(productName);
            tempProducts.forEach(product -> {
                productMapper.insertProduct(product);
                productList.add(product);
            });
        });
        Comparator<Product> comparator = Comparator.comparing(Product::getProductPrice);
        productList.sort(sortByPrice.equalsIgnoreCase("asc")? comparator: comparator.reversed());
        return productList;
    }
}

package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AmazonScrapper  implements Scrapper {
    static final String PRODUCT_URL = "https://www.amazon.com/s?k={productName}";

    // Extract - ID, ProductName, ProductPrice, ProductRatings
    @Override
    public List<Product> scrap(String itemName) {
        // Set up Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.get(PRODUCT_URL.replace("{productName}", itemName));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='puisg-row']")));

        final List<WebElement> elements = driver.findElements(By.cssSelector("div.puisg-row"));

        return elements
                .stream()
                .map(webElement -> {
                    try {
                        final Product product = new Product();
                        WebElement textElement = webElement.findElement(new By.ByXPath(".//div[@data-cy='title-recipe']"));
                        product.setProductName(textElement.getText());

                        WebElement priceElement = webElement.findElement(new By.ByXPath(".//span[@class='a-price-whole']"));
                        product.setProductPrice(priceElement.getText());

                        return product;

                    } catch (NoSuchElementException ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .peek(product -> {
                    final String description = callApi(HttpClient.newBuilder().build(), product.getProductName());
                    product.setProductDescription(description);
                }).toList();
    }

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
}
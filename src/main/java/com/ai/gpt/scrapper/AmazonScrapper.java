package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;
import com.ai.gpt.utils.AppUtils;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AmazonScrapper implements Scrapper {
    private static final Logger logger = LoggerFactory.getLogger(AmazonScrapper.class);
    static final String PRODUCT_URL = "https://www.amazon.com/s?k={productName}";

    @Override
    public List<Product> crawl(String itemName) {
        final ArrayList<Product> scrappedItems = new ArrayList<>();
        final ChromeDriver webDriver = new ChromeDriver(AppUtils.getChromeOptions());
        final String formattedUrl = PRODUCT_URL.replace("{productName}", itemName);
        logger.info("Scraping {}", formattedUrl);

        try {
            webDriver.get(formattedUrl);
            // To bypass Robot Detection - mainly with amazon
            configureCustomHeaders(webDriver);
            // 1. Fetch all bounding divs
            final List<WebElement> elements = webDriver.findElements(By.cssSelector("div.puis-card-container"));
            // 2. Declare Products
            int maxSize = Math.min(5, elements.size());
            for (WebElement webElement : elements) {
                final Product product = new Product();
                try {
                    WebElement textElement = webElement.findElement(new By.ByXPath(".//div[@data-cy='title-recipe']"));
                    product.setName(textElement.getText());

                    WebElement priceElement = webElement.findElement(new By.ByXPath(".//span[@class='a-price-whole']"));
                    product.setPrice(AppUtils.convertPrice(priceElement.getText()));

                    WebElement productURL = webElement.findElement(By.cssSelector("a.a-link-normal"));
                    product.setUrl(AppUtils.shortenURL(productURL.getAttribute("href")));

                    product.setSource("Amazon");
                    product.setLogo("https://crowdiate.com/wp-content/uploads/2020/07/Amazon-Thumbnail.png");
                } catch (NoSuchElementException ex) {
                    logger.warn("Unable to Scrap Product {} {}", product.getName(), ex.getMessage());
                }
                if (product.isValidProduct()) {
                    maxSize -= 1;
                    scrappedItems.add(product);
                }
                if (maxSize == 0) {
                    break;
                }
            }
        } catch (WebDriverException ex) {
            logger.error("Unable to initiate Product Scrapper due to {}", ex.getRawMessage());
        } finally {
            webDriver.quit();
        }
        return scrappedItems;
    }

    private void configureCustomHeaders(final ChromeDriver webDriver) {
        // Get the DevTools from the ChromeDriver
        DevTools devTools = webDriver.getDevTools();
        devTools.createSession();

        // Enable Network domain to be able to intercept network requests
        devTools.send(Network.enable(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()));

        // Set custom headers
        Map<String, Object> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.put("Accept-Language", "en-US,en;q=0.9");
        headers.put("Accept-Encoding", "gzip, deflate, br");

        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
    }
}
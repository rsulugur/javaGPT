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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EbayScrapper implements Scrapper {
    static final String PRODUCT_URL = "https://www.ebay.com/sch/i.html?&LH_BIN=1&_nkw={productName}";
    private static final Logger logger = LoggerFactory.getLogger(EbayScrapper.class);

    @Override
    public List<Product> crawl(String itemName) {
        final ArrayList<Product> scrappedItems = new ArrayList<>();
        final WebDriver webDriver = new ChromeDriver(AppUtils.getChromeOptions());
        try {
            String formattedProductUrl = PRODUCT_URL.replace("{productName}", itemName);
            webDriver.get(formattedProductUrl);
            // 1. Fetch all bounding divs
            final List<WebElement> elements = webDriver.findElements(By.cssSelector("li.s-item.s-item__pl-on-bottom"));

            // 2. Declare Products
            int maxSize = Math.min(5, elements.size());
            for (WebElement webElement : elements) {
                final Product product = new Product();
                try {
                    WebElement textElement = webElement.findElement(By.cssSelector(".s-item__title"));
                    product.setName(textElement.getText());

                    WebElement priceElement = webElement.findElement(By.cssSelector(".s-item__price"));
                    product.setPrice(AppUtils.convertPrice(priceElement.getText()));

                    WebElement productURL = webElement.findElement(By.cssSelector(".s-item__link"));
                    product.setUrl(AppUtils.shortenURL(productURL.getAttribute("href")));

                    product.setSource("Ebay");
                    product.setLogo("https://upload.wikimedia.org/wikipedia/commons/4/48/EBay_logo.png");

                } catch (Exception ex) {
                    logger.warn("Unable to Scrap Ebay Product {}", product.getName());
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
}

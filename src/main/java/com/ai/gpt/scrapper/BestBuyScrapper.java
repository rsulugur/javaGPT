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
@AllArgsConstructor
public class BestBuyScrapper implements  Scrapper{
    static final String PRODUCT_URL = "https://www.bestbuy.com/site/searchpage.jsp?st={productName}";
    private static final Logger logger = LoggerFactory.getLogger(BestBuyScrapper.class);

    @Override
    public List<Product> crawl(String itemName) {
        final ArrayList<Product> scrappedItems = new ArrayList<>();
        final WebDriver webDriver = new ChromeDriver(AppUtils.getChromeOptions());

        try {
            String formattedProductUrl = PRODUCT_URL.replace("{productName}", itemName);
            webDriver.get(formattedProductUrl);
            // 1. Fetch all bounding divs
            final List<WebElement> elements = webDriver.findElements(By.cssSelector("li.sku-item"));

            // 2. Declare Products
            int maxSize = Math.min(5, elements.size());
            for (int i = 0; i < maxSize; i++) {
                WebElement webElement = elements.get(i);
                final Product product = new Product();
                try {
                    WebElement textElement = webElement.findElement(By.cssSelector("h4.sku-title"));
                    product.setName(textElement.getText());

                    WebElement priceElement = webElement.findElement(By.cssSelector("div.priceView-customer-price"));
                    String price = priceElement.findElement(By.cssSelector("span")).getText();
                    product.setPrice(AppUtils.convertPrice(price));

                    WebElement productURL = webElement.findElement(By.cssSelector("h4.sku-title"));
                    String url = productURL.findElement(By.cssSelector("a")).getAttribute("href");
                    product.setUrl(AppUtils.shortenURL(url));

                    product.setSource("BestBuy");
                    product.setLogo("https://corporate.bestbuy.com/wp-content/uploads/2017/03/best-buy-logo.jpg");

                } catch (Exception ex) {
                    logger.warn("Unable to Scrap Ebay Product {}", product.getName());
                }
                if (product.isValidProduct()) {
                    scrappedItems.add(product);
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

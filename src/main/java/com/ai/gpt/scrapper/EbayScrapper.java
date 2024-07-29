package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;
import com.ai.gpt.utils.PriceConverter;
import com.ai.gpt.utils.URLShortener;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class EbayScrapper implements Scrapper {
    static final String PRODUCT_URL = "https://www.ebay.com/sch/i.html?&LH_BIN=1&_nkw={productName}";
    private final ChromeOptions chromeOptions;

    @Override
    public Stream<Product> scrap(String itemName) {
        String formattedItemName = itemName.strip().replace(" ", "+");
        String formattedProductURL = PRODUCT_URL.replace("{productName}", formattedItemName);
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        webDriver.get(formattedProductURL);

        final List<WebElement> elements = webDriver.findElements(By.cssSelector("li.s-item.s-item__pl-on-bottom"));

        return elements
                .parallelStream()
                .<Optional<Product>>map(webElement -> {
                    try {
                        final Product product = new Product();
                        WebElement textElement = webElement.findElement(By.cssSelector(".s-item__title"));
                        product.setProductName(textElement.getText());

                        WebElement priceElement = webElement.findElement(By.cssSelector(".s-item__price"));
                        product.setProductPrice(PriceConverter.convertPrice(priceElement.getText()));

                        WebElement productURL = webElement.findElement(By.cssSelector(".s-item__link"));
                        product.setProductUrl(URLShortener.shortenURL(productURL.getAttribute("href")));
                        // System.out.println("Ebay Scrapping..." + Thread.currentThread().getName());
                        return Optional.of(product);
                    } catch (NoSuchElementException ignored) {
                        return Optional.empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Product::isValidProduct);
    }
}

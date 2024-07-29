package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;
import com.ai.gpt.utils.URLShortener;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AmazonScrapper implements Scrapper {
    static final String PRODUCT_URL = "https://www.amazon.com/s?k={productName}";
    private static final Logger LOGGER = Logger.getLogger(AmazonScrapper.class.getName());
    private final ChromeOptions chromeOptions;

    @Override
    public List<Product> scrap(String itemName) {
        String formattedItemName = itemName.strip().replace(" ", "+");
        String formattedProductURL = PRODUCT_URL.replace("{productName}", formattedItemName);
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        webDriver.get(formattedProductURL);

        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='puisg-row']")));
            return initiateScrapping(webDriver);
        } catch (Exception ex) {
            return List.of();
        }
    }

    private List<Product> initiateScrapping(WebDriver webDriver) {

        final List<WebElement> elements = webDriver.findElements(By.cssSelector("div.puisg-row"));

        return elements
                .stream()
                .<Optional<Product>>map(webElement -> {
                    try {
                        final Product product = new Product();
                        WebElement textElement = webElement.findElement(new By.ByXPath(".//div[@data-cy='title-recipe']"));
                        product.setProductName(textElement.getText());

                        WebElement priceElement = webElement.findElement(new By.ByXPath(".//span[@class='a-price-whole']"));
                        product.setProductPrice(priceElement.getText());

                        WebElement productURL = webElement.findElement(By.cssSelector("a.a-link-normal"));
                        product.setProductUrl(URLShortener.shortenURL(productURL.getAttribute("href")));

                        return Optional.of(product);
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Product::isValidProduct)
                .limit(5)
                .toList();
    }
}
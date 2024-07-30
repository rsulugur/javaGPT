package com.ai.gpt.scrapper;

import com.ai.gpt.model.ErrorObject;
import com.ai.gpt.model.Product;
import com.ai.gpt.utils.PriceConverter;
import com.ai.gpt.utils.URLShortener;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class AmazonScrapper implements Scrapper {
    //    static final String PRODUCT_URL = "https://www.amazon.com/s?k={productName}";
    static final String PRODUCT_URL = "https://www.amazon.com";
    private final ChromeOptions chromeOptions;

    @Override
    public Stream<Product> scrap(String itemName, List<ErrorObject> errorObjects) {
//        String formattedItemName = itemName.strip().replace(" ", "+");
//        String formattedProductURL = PRODUCT_URL.replace("{productName}", formattedItemName);
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        try {
            return initiateScrapping(webDriver, itemName).toList().stream();
        } catch (Exception ex) {
            errorObjects.add(new ErrorObject("Unable to execute Amazon Scrapping" + ex.getMessage()));
            return Stream.of();
        } finally {
            webDriver.quit();
        }
    }

    private Stream<Product> initiateScrapping(WebDriver webDriver, String itemName) {

        webDriver.get(PRODUCT_URL);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(new By.ByCssSelector("input#twotabsearchtextbox")));

        webDriver.findElement(new By.ByCssSelector("input#twotabsearchtextbox")).sendKeys(itemName);
        webDriver.findElement(new By.ByCssSelector("input#nav-search-submit-button")).click();

        final List<WebElement> elements = webDriver.findElements(By.cssSelector("div.puisg-row"));

        return elements.stream()
                .<Optional<Product>>map(webElement -> {
                    try {
                        final Product product = new Product();

                        WebElement textElement = webElement.findElement(new By.ByXPath(".//div[@data-cy='title-recipe']"));
                        product.setProductName(textElement.getText());

                        WebElement priceElement = webElement.findElement(new By.ByXPath(".//span[@class='a-price-whole']"));
                        product.setProductPrice(PriceConverter.convertPrice(priceElement.getText()));

                        WebElement productURL = webElement.findElement(By.cssSelector("a.a-link-normal"));
                        product.setProductUrl(URLShortener.shortenURL(productURL.getAttribute("href")));
                        // System.out.println("Amazon Scrapping..."+ Thread.currentThread().getName());
                        return Optional.of(product);
                    } catch (Exception e) {
                        return Optional.empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Product::isValidProduct)
                .limit(5);
    }
}
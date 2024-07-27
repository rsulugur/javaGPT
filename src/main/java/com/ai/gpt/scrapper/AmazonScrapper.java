package com.ai.gpt.scrapper;

import com.ai.gpt.model.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.ai.gpt.utils.AmazonConstants.*;

@Service
public class AmazonScrapper  implements Scrapper{
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

        driver.manage().window().maximize();
        driver.get(PRODUCT_URL.replace("{productName}", itemName));
        System.out.println("Product URL" + PRODUCT_URL.replace("{productName}", itemName));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // 10 seconds timeout
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-index]")));


        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='puisg-row']"));

        for (WebElement webElement : elements) {
            Product product = new Product();
            WebElement textElement = webElement.findElement(By.xpath("//div[@data-cy='title-recipe']"));
            product.setProductName(textElement.getText());
            System.out.println("Name" + product.getProductName());

            WebElement priceElement = webElement.findElement(By.xpath("//span[@class='a-price']"));
            System.out.println("price" + priceElement.getText());
        }

        return List.of();
    }
}

/*
*         // Prepare the HTML content
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Extracted Elements</title></head><body>");

        for (WebElement element : elements) {
            htmlContent.append(element.getAttribute("outerHTML"));
        }

        htmlContent.append("</body></html>");

        // Save the HTML content to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.html"))) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("HTML content saved to output.html");
*
*
* */
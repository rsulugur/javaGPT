package com.ai.gpt.utils;

import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URI;
import java.net.URISyntaxException;

public class AppUtils {

    public static Float convertPrice(String price) {
        try {
            price = price.trim();
            if (price.startsWith("$")) {
                price = price.substring(1);
            }
            return Float.parseFloat(price);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String shortenURL(String url) {
        try {
            URI uri = new URI(url);
            URI newUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null);
            return newUri.toString();
        } catch (URISyntaxException ex) {
            return url;
        }
    }

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return options;
    }
}

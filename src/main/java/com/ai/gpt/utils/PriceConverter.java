package com.ai.gpt.utils;

public class PriceConverter {

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
}

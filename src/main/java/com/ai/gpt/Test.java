package com.ai.gpt;

import com.ai.gpt.scrapper.AmazonScrapper;
import com.ai.gpt.scrapper.Scrapper;

public class Test {
    public static void main(String[] args) {
        Scrapper scrapper = new AmazonScrapper();
        scrapper.scrap("Iphone 15 Pro max");
    }
}

package com.ai.gpt.utils;

import java.net.URI;
import java.net.URISyntaxException;


public class URLShortener {

    public static String shortenURL(String url) {
        try {
            URI uri = new URI(url);
            URI newUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null);
            return newUri.toString();
        } catch (URISyntaxException ex) {
            return url;
        }
    }
}

package com.baotruongtuan.RdpServer.utils;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class DomainExtractHelper {
    public boolean isValidUrl(String content) {
        try {
            URI uri = new URI(content);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost().replace("www.", "");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
    }
}

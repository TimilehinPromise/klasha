package com.be.klash.http;

import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class Helpers {

    public static URI buildURI(final String baseUrl, final String path, final Map<String, String> requestParameters) throws URISyntaxException {
        final URIBuilder uriBuilder = new URIBuilder(baseUrl + path);
        if (requestParameters != null) {
            requestParameters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    uriBuilder.setParameter(key, value);
                }
            });
        }
        return uriBuilder.build();
    }

    public static URI buildURI(final String baseUrl, final String path) throws URISyntaxException {
        return buildURI(baseUrl, path, null);
    }
}

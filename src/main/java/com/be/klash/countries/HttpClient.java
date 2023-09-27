package com.be.klash.countries;

import com.be.klash.http.Helpers;
import com.be.klash.models.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public class HttpClient {

    private final Config config;
    private final RestTemplate restTemplate;

    public HttpClient(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }


    public ResponseEntity<String> apiCall(final String path, final Object request,
                         HttpHeaders headers,final HttpMethod method){
        String baseUrl = config.getBaseUrl();
        try {
            final ResponseEntity<String> responseEntity = restTemplate.exchange(Helpers.buildURI(baseUrl, path),
                    method, new HttpEntity<>(request, headers), String.class);

            return responseEntity;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }



    public <T> T sendRequest(final String path, final Object request, final Class<T> responseType,
                             HttpHeaders headers, final Map<String, String> additionalHeaderAttribute,
                             final HttpMethod method) {

        String baseUrl = config.getBaseUrl();
        log.info(request + " e");

        String response = null;
        try {
            log.info(Helpers.buildURI(baseUrl, path).toString());
            final ResponseEntity<String> responseEntity = restTemplate.exchange(Helpers.buildURI(baseUrl, path),
                    method, new HttpEntity<>(request, headers), String.class);

            response = responseEntity.getBody();

            if (String.class.equals(responseType)) {
                return (T) response;
            }
            return OBJECT_MAPPER.readValue(response, responseType);
        } catch (HttpClientErrorException  http) {
            response = http.getResponseBodyAsString();
            try {
                APIResponse errorResponse = OBJECT_MAPPER.readValue(response, APIResponse.class);
             //   prePaymentBEResponse.setStatusMessage(StringUtils.defaultString(prePaymentBEResponse.getStatusMessage(), result));
                return (T) errorResponse;
            } catch (Exception jsonex) {
                log.info("exception");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }


    private HttpHeaders createHeaders(Map<String, String> additionalHeaderAttribute) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (additionalHeaderAttribute != null) {
            additionalHeaderAttribute.forEach((k, v) -> headers.set(k, v));
        }

        return headers;
    }

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

    public static class HTTPClientBuilder {

        private final Config config;
        private RestTemplate restTemplate;

        public HTTPClientBuilder(Config config) {
            this.config = config;
        }

        public HTTPClientBuilder restTemplate(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
            return this;
        }

        public HttpClient createClient() {
            return new HttpClient(config, restTemplate);
        }
    }
}

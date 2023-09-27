package com.be.klash.http;


import com.be.klash.models.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Scanner;

@Service
@Slf4j
public class HTTPRestTemplate {

    private static final  int CONNECTION_TIMEOUT = 30000;
    private static final  int REQUEST_TIMEOUT = 30000;
    private static final int SOCKET_TIMEOUT = 30000 ;

    private RestTemplate restTemplate;

    public ClientHttpRequestFactory getClientHttpRequestFactory() {


        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofDays(CONNECTION_TIMEOUT))
                .setConnectionRequestTimeout(Timeout.ofDays(REQUEST_TIMEOUT))
                .build();

        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(HTTPClientConfig.getPoolingHttpClientConnectionManager())
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    public RestTemplate getClient() {
        if (restTemplate == null) {
            this.restTemplate = new RestTemplate(getClientHttpRequestFactory());
        }
        return this.restTemplate;
    }

  }

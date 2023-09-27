package com.be.klash.http;

import com.be.klash.countries.Config;
import com.be.klash.countries.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HTTPClientConfig {

    public static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);
        return connectionManager;
    }

    @Bean
    @Autowired
    public HttpClient getWemaHttpClient(Config config) {
        log.trace(" setting HTTP client for wema disbursemnt service");
        return new HttpClient.HTTPClientBuilder(config)
                .restTemplate(new HTTPRestTemplate().getClient())
                .createClient();
    }
}

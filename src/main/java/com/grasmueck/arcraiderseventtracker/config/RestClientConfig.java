package com.grasmueck.arcraiderseventtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


// This configuration class defines beans for RestClient and its builder, which are used to make HTTP requests to the Metaforge API.
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient metaforgeRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://metaforge.app/api/arc-raiders")
                .defaultHeader("User-Agent", "arc-raiders-event-tracker/1.0")
                .build();
    }
}
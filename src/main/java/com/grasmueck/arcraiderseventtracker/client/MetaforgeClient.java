package com.grasmueck.arcraiderseventtracker.client;

import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;


// Client class to interact with the Metaforge API
@Slf4j
@Component
public class MetaforgeClient {
    public static final String METAFORGE_ALL_SCHEDULED_EVENTS_API_URL = "/events-schedule";

    private final RestClient restClient;

    public MetaforgeClient(RestClient metaforgeRestClient) {
        this.restClient = metaforgeRestClient;
    }

    public MetaforgeResponseDto collectAllEvents() {
        return collectAny(METAFORGE_ALL_SCHEDULED_EVENTS_API_URL);
    }

    // General method to collect data from any specified API endpoint with error handling
    public MetaforgeResponseDto collectAny(String apiName) {

        try {
            return requestApi(apiName);
        } catch (IOException e) {
            log.error("Request failed (I/O): {}", e.getMessage());
        } catch (InterruptedException e) {
            log.error("Request interrupted: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid URL: {} -> {}", restClient.get(), e.getMessage());
        } catch (RuntimeException e) {
            log.error("API error: {}", e.getMessage());
        }

        return null;
    }

    // Helper method to perform the API request and handle API errors
    public MetaforgeResponseDto requestApi(String apiName) throws IOException, InterruptedException {
        return restClient.get()
                .uri(apiName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            throw new RuntimeException("Client error from Metaforge API");
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new RuntimeException("Server error from Metaforge API");
                        })
                .body(MetaforgeResponseDto.class);
    }
}

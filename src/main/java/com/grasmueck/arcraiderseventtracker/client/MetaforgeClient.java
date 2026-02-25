package com.grasmueck.arcraiderseventtracker.client;

import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;



@Component
public class MetaforgeClient {
    public static final String COLLECT_METAFORGE_API_FAILED = "COLLECT_METAFORGE_API_FAILED";
    public static final String METAFORGE_ALL_SCHEDULED_EVENTS_API_URL = "/events-schedule";

    private final RestClient restClient;

    public MetaforgeClient(RestClient metaforgeRestClient) {
        this.restClient = metaforgeRestClient;
    }


    public MetaforgeResponseDto collectAllEvents() {
        return collectAny(METAFORGE_ALL_SCHEDULED_EVENTS_API_URL);
    }

    public MetaforgeResponseDto collectAny(String apiName) {
        try {
            return requestApi(apiName);
        } catch (IOException e) {
            System.err.println("Request failed (I/O): " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URL: " + restClient.get() + " -> " + e.getMessage());
        }

        return null;
    }

    public MetaforgeResponseDto requestApi(String apiName) throws IOException, InterruptedException {
        return restClient.get()
                .uri(apiName)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        (request, response) -> {
                            throw new RuntimeException("Client error from Metaforge API");
                        })
                .onStatus(status -> status.is5xxServerError(),
                        (request, response) -> {
                            throw new RuntimeException("Server error from Metaforge API");
                        })
                .body(MetaforgeResponseDto.class);
    }
}

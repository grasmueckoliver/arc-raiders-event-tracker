package com.grasmueck.arcraiderseventtracker.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CollectMetaforgeApi {
        public static final String COLLECT_METAFORGE_API_FAILED = "COLLECT_METAFORGE_API_FAILED";
        public static final String METAFORGE_ALL_EVENTS_API_URL =
                "https://metaforge.app/api/arc-raiders/events-schedule";

        JSONParser parser = new JSONParser();



    private final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public JSONObject collectAllEvents() {
        return collectAny(METAFORGE_ALL_EVENTS_API_URL);
    }

    public JSONObject collectAny(String urlString) {
        try {
            HttpResponse<String> response = httpGet(urlString);
            System.out.println("Request to: " + urlString);
            System.out.println("Status: " + response.statusCode());
            System.out.println("Headers: " + response.headers().map());

            return (JSONObject) (response.body() == null ? parser.parse("") : parser.parse(response.body()));
        } catch (IOException e) {
            System.err.println("Request failed (I/O): " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid URL: " + urlString + " -> " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Json parse exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public HttpResponse<String> httpGet(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", "arc-raiders-event-tracker/1.0")
                .GET()
                .build();

        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

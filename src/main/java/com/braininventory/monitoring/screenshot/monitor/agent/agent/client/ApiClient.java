package com.braininventory.monitoring.screenshot.monitor.agent.agent.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ApiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String URL = "http://localhost:9090/api/monitor/activity";
    private static final String API_KEY = "SECRET_KEY";

    public void send(Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            log.error("Failed to send data", e);
        }
    }
}
package com.braininventory.monitoring.screenshot.monitor.agent.agent.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiClient {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper;

    @Value("${backend.activity.url}")
    private String url;

    @Value("${backend.api.key}")
    private String apiKey;

    public String send(Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);

            log.debug("Sending HTTP Request -> URL: {}, Payload: {}", url, json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("API Response -> Status: {}, Body: {}",
                    response.statusCode(),
                    response.body());

            return response.body();

        } catch (Exception e) {
            log.error("API call failed for payload={}", payload, e);
            throw new RuntimeException("Failed to send data to backend", e);
        }
    }
}
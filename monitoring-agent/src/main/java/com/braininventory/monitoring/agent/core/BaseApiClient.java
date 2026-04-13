package com.braininventory.monitoring.agent.core;



import com.braininventory.monitoring.common.exception.AgentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


@Slf4j
@RequiredArgsConstructor
public abstract class BaseApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

     protected String post(String url, String apiKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15)) // configurable via HttpClientConfig
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("API Response -> Status: {}, Body: {}", response.statusCode(), response.body());

            if (response.statusCode() >= 400) {
                throw new AgentException(
                        String.format("Backend error [%d]: %s", response.statusCode(), response.body())
                );
            }

            return response.body();

        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("API call interrupted. Payload={}", payload, ex);
            throw new AgentException("Failed to send data to backend");
        } catch (Exception ex) {
            log.error("API call failed. Payload={}", payload, ex);
            throw new AgentException("Unexpected error during API call");
        }
    }

    // ADD THIS GET METHOD
    public <T> T get(String url, String apiKey, Class<T> responseType) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Bearer " + apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return null; // Or throw custom exception
            }

            return objectMapper.readValue(response.body(), responseType);
        } catch (Exception ex) {
            log.error("GET request failed to URL: {}", url, ex);
            return null;
        }
    }
}
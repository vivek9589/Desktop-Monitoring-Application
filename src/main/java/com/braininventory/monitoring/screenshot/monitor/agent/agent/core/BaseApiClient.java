package com.braininventory.monitoring.screenshot.monitor.agent.agent.core;



import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AgentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Base API client to handle HTTP communication with backend.
 * Provides reusable POST functionality.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    protected String post(String url, String apiKey, Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);

            log.debug("API Request -> URL: {}, Payload: {}", url, json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("API Response -> Status: {}", response.statusCode());

            if (response.statusCode() >= 400) {
                log.error("API Error Response: {}", response.body());
                throw new AgentException("Backend returned error: " + response.statusCode());
            }

            return response.body();

        } catch (Exception ex) {
            log.error("API call failed. Payload={}", payload, ex);
            throw new AgentException("Failed to send data to backend");
        }
    }
}

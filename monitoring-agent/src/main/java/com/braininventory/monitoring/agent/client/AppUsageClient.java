package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.agent.core.BaseApiClient;
import com.braininventory.monitoring.common.dto.request.AppActivityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;

@Slf4j
@Component
public class AppUsageClient extends BaseApiClient {

    private final String baseUrl;
    private final String apiKey;

    public AppUsageClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            @Value("${app.base-url}") String baseUrl,
            @Value("${backend.api.key:}") String apiKey) { // Added a default empty string if key isn't provided
        super(httpClient, objectMapper);
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    /**
     * Sends the app activity session to the backend.
     * * @param request The activity data payload
     * @return The response body from the server
     */
    public String send(AppActivityRequest request) {
        String url = baseUrl + "/api/app-usage";

        log.debug("Sending app activity for agent: {}", request.getAgentId());

        // Utilizing the 'post' method from BaseApiClient
        return post(url, apiKey, request);
    }
}
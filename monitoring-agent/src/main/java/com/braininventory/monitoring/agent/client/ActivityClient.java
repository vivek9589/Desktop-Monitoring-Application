package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.agent.core.BaseApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;

@Slf4j
@Component
public class ActivityClient extends BaseApiClient {

    private final String url;
    private final String apiKey;

    public ActivityClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            @Value("${backend.activity.url}") String url,
            @Value("${backend.api.key}") String apiKey
    ) {
        super(httpClient, objectMapper);
        this.url = url;
        this.apiKey = apiKey;
    }

    /**
     * Sends the activity payload using the centralized BaseApiClient logic.
     * * @param payload The ActivityRequest object
     * @return The response body from the backend
     */
    public String send(Object payload) {
        log.debug("ActivityClient: Sending activity payload to {}", url);

        // This calls the 'post' method in BaseApiClient
        return post(url, apiKey, payload);
    }
}
package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.agent.config.TokenManager;
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
    private final TokenManager tokenManager;

    public AppUsageClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            TokenManager tokenManager, // Injecting TokenManager
            @Value("${app.base-url}") String baseUrl) {
        // FIX: Passing all three required beans to the parent class
        super(httpClient, objectMapper, tokenManager);
        this.baseUrl = baseUrl;
        this.tokenManager = tokenManager;
    }

    public String send(AppActivityRequest request) {
        String url = baseUrl + "/api/app-usage";

        // Get dynamic token from local storage
        String dynamicToken = tokenManager.getToken();

        if (dynamicToken == null) {
            log.warn("App usage data not sent: User not authenticated.");
            return null;
        }

        log.debug("Sending app activity for user: {}", request.getAgentId());
        // Use dynamicToken instead of static apiKey
        return post(url, dynamicToken, request);
    }
}
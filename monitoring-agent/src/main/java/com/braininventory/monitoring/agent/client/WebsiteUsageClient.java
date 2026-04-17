package com.braininventory.monitoring.agent.client;


import com.braininventory.monitoring.agent.config.TokenManager;
import com.braininventory.monitoring.agent.core.BaseApiClient;
import com.braininventory.monitoring.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.common.exception.AgentException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;

/**
 * Client responsible for sending website usage data to backend.
 * Agent ONLY sends metadata. No processing here.
 */

@Slf4j
@Component
public class WebsiteUsageClient extends BaseApiClient {

    private final String finalUrl;
    private final TokenManager tokenManager;

    public WebsiteUsageClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            TokenManager tokenManager, // Injected via Spring
            @Value("${app.base-url}") String baseUrl,
            @Value("${backend.website.path:/api/website-usage}") String path
    ) {
        // FIX: Pass all three required beans to the parent class
        super(httpClient, objectMapper, tokenManager);

        this.finalUrl = baseUrl + path;
        this.tokenManager = tokenManager;
    }

    public void send(WebsiteUsageDto dto) {
        try {
            String dynamicToken = tokenManager.getToken();

            if (dynamicToken == null) {
                log.warn("Skipping send: User not authenticated.");
                return;
            }

            log.debug("Sending website usage to: {}", finalUrl);
            // Now using the dynamic token
            post(finalUrl, dynamicToken, dto);
            log.info("Website usage sent successfully for URL: {}", dto.getUrl());
        } catch (Exception ex) {
            log.error("Failed to send website usage", ex);
        }
    }
}
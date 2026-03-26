package com.braininventory.monitoring.screenshot.monitor.agent.agent.client;


import com.braininventory.monitoring.screenshot.monitor.agent.agent.core.BaseApiClient;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AgentException;
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
    private final String apiKey;

    public WebsiteUsageClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            @Value("${app.base-url}") String baseUrl, // Inject the base (http://localhost:9090)
            @Value("${backend.website.path:/api/website-usage}") String path, // The specific path
            @Value("${backend.api.key}") String apiKey
    ) {
        super(httpClient, objectMapper);
        // Combine them to form: http://localhost:9090/api/website-usage
        this.finalUrl = baseUrl + path;
        this.apiKey = apiKey;
    }

    public void send(WebsiteUsageDto dto) {
        try {
            log.debug("Sending website usage to: {}", finalUrl);
            post(finalUrl, apiKey, dto);
            log.info("Website usage sent successfully for URL: {}", dto.getUrl());
        } catch (AgentException ex) {
            log.warn("Backend rejected website usage: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Failed to send website usage to {}", finalUrl, ex);
        }
    }
}
package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.agent.config.TokenManager; // Import your TokenManager
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
    private final TokenManager tokenManager; // 1. Inject TokenManager

    public ActivityClient(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            TokenManager tokenManager, // 2. Add to constructor
            @Value("${backend.activity.url}") String url
    ) {
        // 3. FIX: Pass all three required beans to the parent class
        super(httpClient, objectMapper, tokenManager);
        this.url = url;
        this.tokenManager = tokenManager;
    }

    /**
     * Sends the activity payload using the dynamic token from TokenManager.
     */
    public String send(Object payload) {
        // 4. Get the token dynamically instead of using hardcoded apiKey
        String dynamicToken = tokenManager.getToken();

        if (dynamicToken == null) {
            log.warn("Activity data not sent: User is not authenticated.");
            return null;
        }

        log.debug("ActivityClient: Sending activity payload to {}", url);

        // 5. Pass the dynamic token to the post method
        return post(url, dynamicToken, payload);
    }
}
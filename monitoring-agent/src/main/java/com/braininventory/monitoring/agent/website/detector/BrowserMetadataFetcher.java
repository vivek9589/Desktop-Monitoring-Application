package com.braininventory.monitoring.agent.website.detector;

import com.braininventory.monitoring.agent.apptracking.detector.ActiveWindowDetector;
import com.braininventory.monitoring.agent.core.BaseApiClient;
import com.braininventory.monitoring.common.dto.request.ActiveTabDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter to fetch browser-related metadata from active window.
 */
@Slf4j
@Component
public class BrowserMetadataFetcher {
    private final ActiveWindowDetector activeWindowDetector;
    private final BrowserDetector browserDetector;
    private final BaseApiClient apiClient; // Your Feign/Rest client pointing to Server

    @Value("${app.base-url}") String baseUrl;
    @Value("${backend.api.key:}") String apiKey;

    // Explicitly define the constructor to use @Qualifier
    public BrowserMetadataFetcher(
            ActiveWindowDetector activeWindowDetector,
            BrowserDetector browserDetector,
            @Qualifier("activityClient") BaseApiClient apiClient) { // <--- ADD THIS
        this.activeWindowDetector = activeWindowDetector;
        this.browserDetector = browserDetector;
        this.apiClient = apiClient;
    }

    public BrowserMetadata getCurrent() {
        String app = Optional.ofNullable(activeWindowDetector.getActiveApp()).orElse("unknown");
        String title = Optional.ofNullable(activeWindowDetector.getWindowTitle()).orElse("unknown");

        String url = title; // fallback

        // Only call the server if the active app is actually a browser
        if (browserDetector.isBrowser(app)) {
            // Inside BrowserMetadataFetcher.java
            try {
                // Assuming you have a way to get the API Key in the agent
                ActiveTabDto remoteTab = apiClient.get(baseUrl + "/api/active-tab/current", apiKey, ActiveTabDto.class);
                if (remoteTab != null) {
                    url = remoteTab.getUrl();
                }
            } catch (Exception e) {
                log.warn("Failed to sync with browser extension via server");
            }
        }

        return new BrowserMetadata(app, title, url, browserDetector.isBrowser(app));
    }
}
package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.detector;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.ActiveTabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter to fetch browser-related metadata from active window.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BrowserMetadataFetcher {

    private final ActiveWindowDetector activeWindowDetector;
    private final BrowserDetector browserDetector;
    private final ActiveTabService activeTabService;

    public BrowserMetadata getCurrent() {
        String app = Optional.ofNullable(activeWindowDetector.getActiveApp()).orElse("unknown");
        String title = Optional.ofNullable(activeWindowDetector.getWindowTitle()).orElse("unknown");

        String url = activeTabService.getCurrentUrl();
        if (url == null || url.isBlank()) {
            url = title; // fallback
        }

        boolean isBrowser = browserDetector.isBrowser(app);

        return new BrowserMetadata(app, title, url, isBrowser);
    }
}
package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.detector;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
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

    public BrowserMetadata getCurrent() {
        String app = Optional.ofNullable(activeWindowDetector.getActiveApp()).orElse("unknown");
        String title = Optional.ofNullable(activeWindowDetector.getWindowTitle()).orElse("unknown");

        boolean isBrowser = browserDetector.isBrowser(app);

        log.debug("Fetched metadata -> App: {}, Title: {}, IsBrowser: {}", app, title, isBrowser);
        return new BrowserMetadata(app, title, isBrowser);
    }
}
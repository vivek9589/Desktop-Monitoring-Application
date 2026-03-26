package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.scheduler;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.website.detector.BrowserMetadata;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.website.detector.BrowserMetadataFetcher;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.website.state.WebsiteSessionManager;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AgentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler responsible for tracking website usage.
 * Runs at fixed intervals and feeds data to session manager.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebsiteTrackingScheduler {

    private final BrowserMetadataFetcher metadataFetcher;
    private final WebsiteSessionManager sessionManager;

    @Value("${agent.website.interval:5000}")
    private long interval;

    @Scheduled(fixedDelayString = "${agent.website.interval:5000}")
    public void trackWebsite() {
        try {
            BrowserMetadata metadata = metadataFetcher.getCurrent();
            String title = metadata.title();
            boolean isBrowser = metadata.isBrowser();

            String url = normalize(title);
            log.debug("Tracking -> URL: {}, Browser: {}, Interval: {} ms", url, isBrowser, interval);

            sessionManager.handle(url, title, isBrowser);
        } catch (Exception ex) {
            log.error("Website tracking scheduler failed", ex);
            throw new AgentException("Scheduler failure: " + ex.getMessage());
        }
    }

    private String normalize(String title) {
        if (title == null || title.isBlank()) return "unknown";
        return title
                .replace(" - Google Chrome", "")
                .replace(" - Chrome", "")
                .replace(" - Microsoft Edge", "")
                .replace(" - Mozilla Firefox", "")
                .trim();
    }
}
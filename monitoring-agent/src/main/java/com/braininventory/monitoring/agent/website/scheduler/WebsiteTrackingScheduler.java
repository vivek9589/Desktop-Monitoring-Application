package com.braininventory.monitoring.agent.website.scheduler;
import com.braininventory.monitoring.agent.config.TokenManager;
import com.braininventory.monitoring.agent.website.detector.BrowserMetadata;
import com.braininventory.monitoring.agent.website.detector.BrowserMetadataFetcher;
import com.braininventory.monitoring.agent.website.state.WebsiteSessionManager;
import com.braininventory.monitoring.common.exception.AgentException;
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
    private final TokenManager tokenManager;



    @Value("${agent.website.interval:5000}")
    private long interval;

    @Scheduled(fixedDelayString = "${agent.website.interval:5000}")
    public void trackWebsite() {
        // PRE-CONDITION: If no token, don't waste CPU resources
        if (!tokenManager.isAuthenticated()) {
            log.trace("User not authenticated. Skipping website tracking.");
            return;
        }

        try {
            BrowserMetadata metadata = metadataFetcher.getCurrent();
            String title = metadata.title();
            boolean isBrowser = metadata.isBrowser();

            String url = metadata.url() != null ? metadata.url() : normalize(title);

            sessionManager.handle(url, title, isBrowser);
        } catch (Exception ex) {
            log.error("Website tracking scheduler failed", ex);
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
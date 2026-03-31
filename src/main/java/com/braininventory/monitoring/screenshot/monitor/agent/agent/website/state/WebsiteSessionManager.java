package com.braininventory.monitoring.screenshot.monitor.agent.agent.website.state;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.ActivityState;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.client.WebsiteUsageClient;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Manages website session lifecycle.
 * Handles start/stop logic and sends data to backend.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebsiteSessionManager {

    private final WebsiteUsageClient websiteUsageClient;
    private WebsiteSession currentSession;

    @Value("${agent.id}")
    private String agentId;

    public void handle(String url, String title, boolean isBrowser) {
        try {
            boolean isIdle = ActivityState.isIdle();
            log.debug("Handle called -> URL: {}, Browser: {}, Idle: {}", url, isBrowser, isIdle);

            if (!isBrowser || isIdle) {
                endSession("Browser inactive or user idle");
                return;
            }

            if (currentSession == null) {
                startSession(url, title);
                return;
            }

            if (!currentSession.getUrl().equals(url)) {
                log.debug("URL changed from {} → {}", currentSession.getUrl(), url);
                endSession("URL changed");
                startSession(url, title);
            }
        } catch (Exception ex) {
            log.error("Error in WebsiteSessionManager", ex);
        }
    }

    private void startSession(String url, String title) {
        currentSession = new WebsiteSession();
        currentSession.setUrl(url);
        currentSession.setTitle(title);
        currentSession.setStartTime(WebsiteSession.nowIST());

        log.info("Website session STARTED -> {}", url);
    }

    private void endSession(String reason) {
        if (currentSession == null) return;

        try {
            currentSession.setEndTime(WebsiteSession.nowIST());
            long durationSeconds = currentSession.getDurationSeconds();

            if (durationSeconds < 1) {
                log.debug("Ignoring short session (<1s) for {}", currentSession.getUrl());
                currentSession = null;
                return;
            }

            WebsiteUsageDto dto = WebsiteUsageDto.builder()
                    .agentId(this.agentId)
                    .url(currentSession.getUrl())
                    .title(currentSession.getTitle())
                    .startTime(currentSession.getStartTime())
                    .endTime(currentSession.getEndTime())
                    .durationSeconds(durationSeconds)
                    .build();

            websiteUsageClient.send(dto);

            log.info("Website session ENDED [{}] -> {} | Duration: {} sec",
                    reason, currentSession.getUrl(), durationSeconds);

        } catch (Exception ex) {
            log.error("Failed to end session", ex);
        } finally {
            currentSession = null;
        }
    }
}
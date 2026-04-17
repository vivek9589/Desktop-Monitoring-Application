package com.braininventory.monitoring.agent.apptracking.scheduler;


import com.braininventory.monitoring.agent.activity.ActivityState;
import com.braininventory.monitoring.agent.apptracking.detector.ActiveWindowDetector;
import com.braininventory.monitoring.agent.apptracking.state.AppTrackingState;
import com.braininventory.monitoring.agent.client.AppUsageClient;
import com.braininventory.monitoring.agent.config.AuthContext;
import com.braininventory.monitoring.agent.config.TokenManager;
import com.braininventory.monitoring.common.dto.request.AppActivityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppTrackingScheduler {

    private final ActiveWindowDetector detector;
    private final AppUsageClient appUsageClient;
    private final AuthContext authContext; // Dynamic token data
    private final TokenManager tokenManager;

    @Scheduled(fixedDelayString = "${agent.app.interval:5000}")
    public void trackApp() {
        // Pre-check: Don't track if not logged in
        if (!tokenManager.isAuthenticated()) {
            return;
        }

        if (ActivityState.isIdle()) {
            log.info("User is IDLE → Skipping app tracking");
            return;
        }

        String app = cleanAppName(detector.getActiveApp());
        String title = cleanTitle(detector.getWindowTitle());

        if (AppTrackingState.isFirstRun()) {
            AppTrackingState.startSession(app, title);
            return;
        }

        if (AppTrackingState.isAppChanged(app)) {
            LocalDateTime endTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

            long duration = Duration.between(
                    AppTrackingState.getStartTime(),
                    endTime
            ).getSeconds();

            if (duration < 5) {
                AppTrackingState.startSession(app, title);
                return;
            }

            // USE DYNAMIC USER AND ORG IDs
            appUsageClient.send(
                    AppActivityRequest.builder()
                            .agentId(authContext.getUserId())           // From JWT
                            //.organizationId(authContext.getOrganizationId()) // From JWT
                            .appName(app)
                            .windowTitle(title)
                            .startTime(AppTrackingState.getStartTime())
                            .endTime(endTime)
                            .durationSeconds(duration)
                            .build()
            );

            log.info("App session sent for user {}: {} | {} sec", authContext.getUserId(), app, duration);
            AppTrackingState.startSession(app, title);
        }
    }

    private String cleanTitle(String title) {
        if (title == null || title.isEmpty()) return "unknown";
        return title.split("-")[0].trim();
    }

    private String cleanAppName(String app) {
        if (app == null) return "unknown";
        return app.replace(".exe", "").replace(".EXE", "").trim();
    }
}
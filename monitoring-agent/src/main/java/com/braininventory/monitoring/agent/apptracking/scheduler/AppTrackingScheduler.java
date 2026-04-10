package com.braininventory.monitoring.agent.apptracking.scheduler;


import com.braininventory.monitoring.agent.activity.ActivityState;
import com.braininventory.monitoring.agent.apptracking.detector.ActiveWindowDetector;
import com.braininventory.monitoring.agent.apptracking.state.AppTrackingState;
import com.braininventory.monitoring.agent.client.AppUsageClient;
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

    @Value("${agent.id}")
    private String agentId;

    // @Scheduled(fixedRate = 5000)
    @Scheduled(fixedDelayString = "${agent.app.interval:5000}")
    public void trackApp() {

        //  1. Check IDLE (USE YOUR EXISTING LOGIC)
        if (ActivityState.isIdle()) {
            log.info("User is IDLE → Skipping app tracking");
            return;
        }

        //  2. Get current app
        String rawApp = detector.getActiveApp();
        String rawTitle = detector.getWindowTitle();

        String app = cleanAppName(rawApp);
        String title = cleanTitle(rawTitle);

        //  3. First run
        if (AppTrackingState.isFirstRun()) {
            AppTrackingState.startSession(app, title);
            return;
        }

        // 4. If app changed → CLOSE previous session
        if (AppTrackingState.isAppChanged(app)) {

            LocalDateTime endTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

            long duration = Duration.between(
                    AppTrackingState.getStartTime(),
                    endTime
            ).getSeconds();

            //  Ignore small sessions
            if (duration < 5) {
                AppTrackingState.startSession(app, title);
                return;
            }

            // Send to backend
            appUsageClient.send(
                    AppActivityRequest.builder()
                            .agentId(agentId)
                            .appName(app)
                            .windowTitle(title)
                            .startTime(AppTrackingState.getStartTime())
                            .endTime(endTime)
                            .durationSeconds(duration)
                            .build()
            );

            log.info("App session sent: {} | {} sec", app, duration);

            // Start new session
            AppTrackingState.startSession(app, title);
        }
    }

    private String cleanTitle(String title) {
        if (title == null || title.isEmpty()) return "unknown";

        if (title.contains("-")) {
            return title.split("-")[0].trim();
        }

        return title;
    }

    private String cleanAppName(String app) {
        if (app == null) return "unknown";

        return app
                .replace(".exe", "")
                .replace(".EXE", "")
                .trim();
    }
}
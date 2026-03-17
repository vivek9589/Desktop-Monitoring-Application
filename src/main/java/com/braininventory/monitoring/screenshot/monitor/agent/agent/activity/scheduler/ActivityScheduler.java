package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.scheduler;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.ActivityState;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.model.ActivityRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
@Slf4j
public class ActivityScheduler {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.activity.url}")
    private String backendUrl;

    @Scheduled(fixedRate = 5000)
    public void sendActivity() {

        int keyboard = ActivityState.getAndResetKeyboard();
        int mouse = ActivityState.getAndResetMouse();
        boolean idle = ActivityState.isIdle();

        log.info("Keyboard: {}, Mouse: {}, Idle: {}", keyboard, mouse, idle);
        ActivityRequest request = ActivityRequest.builder()
                .agentId("agent-001")
                .keyboardCount(keyboard)
                .mouseCount(mouse)
                .idle(idle)
                .timestamp(LocalDateTime.now())
                .build();

        try {
            log.info("Sending activity data to backend...");

            ResponseEntity<String> response = restTemplate.postForEntity(
                    backendUrl + "/api/monitor/activity",
                    request,
                    String.class
            );

            log.info("Backend Response: {}", response.getBody());

        } catch (Exception e) {
            log.error("Failed to send activity data", e);
        }
    }
}
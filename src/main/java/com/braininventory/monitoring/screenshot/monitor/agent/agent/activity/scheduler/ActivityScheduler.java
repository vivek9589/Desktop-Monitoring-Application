package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.scheduler;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.ActivityState;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.model.ActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.client.ApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Slf4j
public class ActivityScheduler {

    private final ApiClient apiClient;

    @Value("${agent.id}")
    private String agentId;

    @Scheduled(fixedRate = 5000)
    public void sendActivity() {

        int keyboard = ActivityState.getAndResetKeyboard();
        int mouse = ActivityState.getAndResetMouse();
        boolean idle = ActivityState.isIdle();

        log.info("Activity Data -> Keyboard: {}, Mouse: {}, Idle: {}", keyboard, mouse, idle);

        ActivityRequest request = ActivityRequest.builder()
                .agentId(agentId)
                .keyboardCount(keyboard)
                .mouseCount(mouse)
                .idle(idle)
                .timestamp(LocalDateTime.now())
                .build();

        try {
            log.info("Sending activity data to backend via ApiClient... Payload={}", request);

            String response = apiClient.send(request);

            log.info("Backend Response: {}", response);

        } catch (Exception e) {
            log.error("Failed to send activity data to backend. Payload={}", request, e);
        }


        /*
        ================== ALTERNATIVE (RestTemplate - Reference Only) ==================

        ResponseEntity<String> response = restTemplate.postForEntity(
                backendUrl + "/api/monitor/activity",
                request,
                String.class
        );

        log.info("Backend Response: {}", response.getBody());

        ============================================================================
        */
    }
}
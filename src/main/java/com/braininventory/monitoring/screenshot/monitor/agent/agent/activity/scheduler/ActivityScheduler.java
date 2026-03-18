package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.scheduler;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.ActivityState;
import com.braininventory.monitoring.screenshot.monitor.agent.agent.client.ApiClient;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.dto.request.ActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.dto.request.IdleSessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ActivityScheduler {

    private final ApiClient apiClient;

    @Value("${agent.id}")
    private String agentId;

    @Scheduled(fixedRate = 5000)
    public void sendActivity() {

        // 1. Collect data
        int keyboard = ActivityState.getAndResetKeyboard();
        int mouse = ActivityState.getAndResetMouse();
        boolean idle = ActivityState.isIdle();

        //  2. Get idle sessions
        List<IdleSessionDto> idleSessions = ActivityState.getAndResetIdleSessions()
                .stream()
                .map(session -> IdleSessionDto.builder()
                        .startTime(session.getStartTime())
                        .endTime(session.getEndTime())
                        .durationSeconds(session.getDurationSeconds())
                        .build())
                .collect(Collectors.toList());

        log.info("Activity Data -> Keyboard: {}, Mouse: {}, Idle: {}, IdleSessions: {}",
                keyboard, mouse, idle, idleSessions.size());

        //  3. Build request
        ActivityRequest request = ActivityRequest.builder()
                .agentId(agentId)
                .keyboardCount(keyboard)
                .mouseCount(mouse)
                .idle(idle)
                .timestamp(LocalDateTime.now())
                .idleSessions(idleSessions)
                .build();

        try {
            log.info("Sending activity data to backend... Payload={}", request);

            String response = apiClient.send(request);

            log.info("Backend Response: {}", response);

        } catch (Exception e) {
            log.error("Failed to send activity data to backend. Payload={}", request, e);
        }
    }

    // ✅ OPTIONAL: Separate scheduler for idle detection (VERY IMPORTANT)
    @Scheduled(fixedRate = 5000)
    public void checkIdleState() {
        ActivityState.checkIdle();
    }
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
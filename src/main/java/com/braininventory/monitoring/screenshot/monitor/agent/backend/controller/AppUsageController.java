package com.braininventory.monitoring.screenshot.monitor.agent.backend.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.AppActivityRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppUsageSession;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.AppUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/app-usage")
@RequiredArgsConstructor
@Slf4j
public class AppUsageController {

    private final AppUsageService service;

    @PostMapping
    public ApiResponse<String> save(@RequestBody AppActivityRequest request) {
        String requestId = UUID.randomUUID().toString();
        log.info("Received app usage request [{}]: {}", requestId, request);

        try {
            AppUsageSession session = AppUsageSession.builder()
                    .agentId(request.getAgentId())
                    .appName(request.getAppName())
                    .windowTitle(request.getWindowTitle())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .durationSeconds(request.getDurationSeconds())
                    .build();

            service.save(session);

            log.info("Successfully saved app usage session [{}] for agentId={}", requestId, request.getAgentId());
            return ApiResponse.success("App usage saved", requestId);

        } catch (Exception e) {
            log.error("Error saving app usage session [{}]: {}", requestId, e.getMessage(), e);
            return ApiResponse.error(
                    "APP_USAGE_SAVE_ERROR",
                    "Failed to save app usage session",
                    e.getMessage(),
                    requestId
            );
        }
    }
}


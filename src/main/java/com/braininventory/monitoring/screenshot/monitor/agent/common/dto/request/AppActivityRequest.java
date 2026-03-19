package com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppActivityRequest {

    private String agentId;

    private String appName;
    private String windowTitle;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private long durationSeconds;
}

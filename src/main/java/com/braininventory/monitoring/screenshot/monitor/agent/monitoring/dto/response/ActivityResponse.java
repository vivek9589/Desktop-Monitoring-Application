package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityResponse {
    private String agentId;
    private String status;
    private LocalDateTime timestamp;
}

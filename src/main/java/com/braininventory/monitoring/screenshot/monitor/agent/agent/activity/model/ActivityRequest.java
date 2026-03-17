package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityRequest {
    private String agentId;
    private int keyboardCount;
    private int mouseCount;
    private boolean idle;
    private LocalDateTime timestamp;
}

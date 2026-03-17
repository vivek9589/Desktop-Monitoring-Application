package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityRequest {
    private String agentId;
    private int keyboardCount;
    private int mouseCount;
    private LocalDateTime timestamp;
}

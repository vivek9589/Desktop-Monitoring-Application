package com.braininventory.monitoring.screenshot.monitor.agent.agent.activity.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityPayload {

    private String agentId;
    private int keyboardCount;
    private int mouseCount;
    private boolean idle;
    private long timestamp;
}
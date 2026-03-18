package com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ActivityRequest {
    private String agentId;
    private int keyboardCount;
    private int mouseCount;
    private boolean idle;
    private LocalDateTime timestamp;

    private List<IdleSessionDto> idleSessions;
}

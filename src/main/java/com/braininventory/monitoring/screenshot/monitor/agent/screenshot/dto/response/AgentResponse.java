package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentResponse {
    private String agentId;
    private String machineName;
    private String userName;
    private String ipAddress;
    private LocalDateTime lastSeen;
}
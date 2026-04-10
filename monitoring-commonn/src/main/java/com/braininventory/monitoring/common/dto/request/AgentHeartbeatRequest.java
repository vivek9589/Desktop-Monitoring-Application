package com.braininventory.monitoring.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentHeartbeatRequest {
    private String agentId;
    private String machineName;
    private String userName;
}



package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.AgentResponse;

import java.util.List;

public interface AgentService {
    AgentResponse registerOrUpdateAgent(AgentHeartbeatRequest request, String ipAddress);
    List<AgentResponse> getAllAgents();
}
package com.braininventory.monitoring.screenshot.monitor.agent.service;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.AgentResponse;

import java.util.List;

public interface AgentService {
    AgentResponse registerOrUpdateAgent(AgentHeartbeatRequest request, String ipAddress);
    List<AgentResponse> getAllAgents();
}
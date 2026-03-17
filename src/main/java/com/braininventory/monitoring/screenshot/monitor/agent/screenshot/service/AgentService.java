package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service;

import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.response.AgentResponse;

import java.util.List;

public interface AgentService {
    AgentResponse registerOrUpdateAgent(AgentHeartbeatRequest request, String ipAddress);
    List<AgentResponse> getAllAgents();
}
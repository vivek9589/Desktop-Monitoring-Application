package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.request.AgentHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.response.AgentResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.entity.Agent;
import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.repository.AgentRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.service.AgentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public AgentResponse registerOrUpdateAgent(AgentHeartbeatRequest request, String ipAddress) {
        logger.info("Processing heartbeat for agent: {}", request.getAgentId());

        Agent agent = agentRepository.findByAgentId(request.getAgentId())
                .orElse(Agent.builder()
                        .agentId(request.getAgentId())
                        .machineName(request.getMachineName())
                        .userName(request.getUserName())
                        .ipAddress(ipAddress)
                        .createdAt(LocalDateTime.now())
                        .build());

        agent.setLastSeen(LocalDateTime.now());
        Agent saved = agentRepository.save(agent);

        return AgentResponse.builder()
                .agentId(saved.getAgentId())
                .machineName(saved.getMachineName())
                .userName(saved.getUserName())
                .ipAddress(saved.getIpAddress())
                .lastSeen(saved.getLastSeen())
                .build();
    }

    @Override
    public List<AgentResponse> getAllAgents() {
        logger.info("Fetching all agents");
        return agentRepository.findAll().stream()
                .map(agent -> AgentResponse.builder()
                        .agentId(agent.getAgentId())
                        .machineName(agent.getMachineName())
                        .userName(agent.getUserName())
                        .ipAddress(agent.getIpAddress())
                        .lastSeen(agent.getLastSeen())
                        .build())
                .toList();
    }
}
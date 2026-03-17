package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.screenshot.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByAgentId(String agentId);
}


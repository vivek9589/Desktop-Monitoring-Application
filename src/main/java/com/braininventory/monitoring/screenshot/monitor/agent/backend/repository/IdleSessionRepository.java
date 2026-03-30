package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.IdleSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdleSessionRepository extends JpaRepository<IdleSession, Long> {
    Optional<IdleSession> findTopByAgentIdOrderByStartTimeDesc(String agentId);
}

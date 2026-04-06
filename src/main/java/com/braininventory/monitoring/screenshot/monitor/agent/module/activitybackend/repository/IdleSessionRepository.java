package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.IdleSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdleSessionRepository extends JpaRepository<IdleSession, Long> {
    Optional<IdleSession> findTopByAgentIdOrderByStartTimeDesc(String agentId);
}

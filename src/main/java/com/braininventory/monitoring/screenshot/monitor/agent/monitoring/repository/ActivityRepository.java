package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.ActivityLog;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.IdleSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {
}


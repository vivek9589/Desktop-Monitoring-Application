package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {
}


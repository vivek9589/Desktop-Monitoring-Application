package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsageRepository extends JpaRepository<AppUsageSession,Long> {
}

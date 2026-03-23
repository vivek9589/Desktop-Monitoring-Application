package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsageRepository extends JpaRepository<AppUsageSession,Long> {
}

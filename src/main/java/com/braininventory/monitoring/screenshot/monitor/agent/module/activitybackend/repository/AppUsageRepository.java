package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsageRepository extends JpaRepository<AppUsageSession,Long> {
}

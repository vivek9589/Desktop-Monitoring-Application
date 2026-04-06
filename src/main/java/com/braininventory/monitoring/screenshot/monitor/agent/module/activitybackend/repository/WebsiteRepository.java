package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.WebsiteUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<WebsiteUsage,Long> {
}

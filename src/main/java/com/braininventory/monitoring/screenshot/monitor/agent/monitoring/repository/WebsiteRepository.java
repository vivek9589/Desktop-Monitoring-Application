package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.WebsiteUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<WebsiteUsage,Long> {
}

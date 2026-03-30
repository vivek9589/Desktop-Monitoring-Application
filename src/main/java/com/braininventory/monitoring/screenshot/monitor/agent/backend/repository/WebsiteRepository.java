package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.WebsiteUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<WebsiteUsage,Long> {
}

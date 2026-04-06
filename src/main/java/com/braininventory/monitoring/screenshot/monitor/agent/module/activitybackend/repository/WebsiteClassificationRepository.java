package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.WebsiteClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebsiteClassificationRepository
        extends JpaRepository<WebsiteClassification, Long> {

    Optional<WebsiteClassification> findByDomain(String domain);
}

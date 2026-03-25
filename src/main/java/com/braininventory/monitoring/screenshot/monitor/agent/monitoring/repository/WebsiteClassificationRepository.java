package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.WebsiteClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebsiteClassificationRepository
        extends JpaRepository<WebsiteClassification, Long> {

    Optional<WebsiteClassification> findByDomain(String domain);
}

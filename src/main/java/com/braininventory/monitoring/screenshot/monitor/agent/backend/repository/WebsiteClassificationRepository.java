package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.WebsiteClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebsiteClassificationRepository
        extends JpaRepository<WebsiteClassification, Long> {

    Optional<WebsiteClassification> findByDomain(String domain);
}

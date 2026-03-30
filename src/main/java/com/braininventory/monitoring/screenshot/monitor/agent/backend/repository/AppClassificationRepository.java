package com.braininventory.monitoring.screenshot.monitor.agent.backend.repository;


import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppClassificationRepository extends JpaRepository<AppClassification, Long> {
    Optional<AppClassification> findByAppName(String appName);
}

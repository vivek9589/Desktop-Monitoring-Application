package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository;


import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.AppClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppClassificationRepository extends JpaRepository<AppClassification, Long> {
    Optional<AppClassification> findByAppName(String appName);
}

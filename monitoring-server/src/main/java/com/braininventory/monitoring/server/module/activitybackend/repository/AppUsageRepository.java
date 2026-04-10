package com.braininventory.monitoring.server.module.activitybackend.repository;

import com.braininventory.monitoring.server.module.activitybackend.entity.AppUsageSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsageRepository extends JpaRepository<AppUsageSession,Long> {
}

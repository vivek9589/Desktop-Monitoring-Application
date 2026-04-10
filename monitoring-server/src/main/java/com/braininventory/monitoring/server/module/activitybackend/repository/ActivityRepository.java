package com.braininventory.monitoring.server.module.activitybackend.repository;

import com.braininventory.monitoring.server.module.activitybackend.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<ActivityLog, Long> {
}


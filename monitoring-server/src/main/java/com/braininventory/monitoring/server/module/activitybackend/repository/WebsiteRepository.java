package com.braininventory.monitoring.server.module.activitybackend.repository;

import com.braininventory.monitoring.server.module.activitybackend.entity.WebsiteUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<WebsiteUsage,Long> {
}

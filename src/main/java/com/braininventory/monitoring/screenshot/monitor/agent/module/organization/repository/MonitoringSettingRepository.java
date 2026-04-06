package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.MonitoringSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonitoringSettingRepository extends JpaRepository<MonitoringSetting, UUID> {
}
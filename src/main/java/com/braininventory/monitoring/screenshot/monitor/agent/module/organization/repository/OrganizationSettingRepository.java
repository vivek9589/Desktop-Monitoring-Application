package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.OrganizationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationSettingRepository extends JpaRepository<OrganizationSetting, UUID> {

}
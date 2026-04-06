package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {


}
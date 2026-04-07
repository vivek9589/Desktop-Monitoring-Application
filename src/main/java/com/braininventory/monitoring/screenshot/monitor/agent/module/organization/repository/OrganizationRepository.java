package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    List<Organization> findByIsActiveTrue();
    List<Organization> findByNameContainingIgnoreCase(String name);
    List<Organization> findByCityIgnoreCase(String city);
    List<Organization> findByCountryIgnoreCase(String country);

}
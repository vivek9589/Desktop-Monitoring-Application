package com.braininventory.monitoring.server.module.organization.repository;

import com.braininventory.monitoring.server.module.organization.entity.OrganizationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationSettingRepository extends JpaRepository<OrganizationSetting, UUID> {
    Optional<OrganizationSetting> findByOrganization_Id(UUID organizationId);
    boolean existsByOrganization_Id(UUID organizationId);
    void deleteByOrganization_Id(UUID organizationId);
}

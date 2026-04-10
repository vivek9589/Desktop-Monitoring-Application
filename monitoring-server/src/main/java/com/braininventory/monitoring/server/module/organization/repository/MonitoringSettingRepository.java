package com.braininventory.monitoring.server.module.organization.repository;


import com.braininventory.monitoring.server.module.organization.entity.MonitoringSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MonitoringSettingRepository extends JpaRepository<MonitoringSetting, UUID> {
    Optional<MonitoringSetting> findByOrganization_Id(UUID organizationId);
    boolean existsByOrganization_Id(UUID organizationId);
    void deleteByOrganization_Id(UUID organizationId);
}

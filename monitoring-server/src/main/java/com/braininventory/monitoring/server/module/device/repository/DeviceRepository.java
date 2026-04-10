package com.braininventory.monitoring.server.module.device.repository;

import com.braininventory.monitoring.server.module.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;


import java.util.Optional;


public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByDeviceIdentifier(String deviceIdentifier);
}


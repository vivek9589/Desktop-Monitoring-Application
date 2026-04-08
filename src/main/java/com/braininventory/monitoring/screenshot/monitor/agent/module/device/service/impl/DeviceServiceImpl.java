package com.braininventory.monitoring.screenshot.monitor.agent.module.device.service.impl;


import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.DeviceAlreadyExistsException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.DeviceNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.DeviceCategory;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.dto.request.DeviceHeartbeatRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.dto.response.DeviceResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.entity.Device;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.repository.DeviceRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceResponse createDevice(DeviceHeartbeatRequest request, String ipAddress) {
        log.info("Creating new device with identifier: {}", request.getDeviceIdentifier());

        deviceRepository.findByDeviceIdentifier(request.getDeviceIdentifier())
                .ifPresent(existing -> {
                    throw new DeviceAlreadyExistsException(
                            "Device with identifier already exists: " + request.getDeviceIdentifier()
                    );
                });

        Device device = Device.builder()
                .deviceIdentifier(request.getDeviceIdentifier())
                .machineName(request.getMachineName())
                .userName(request.getUserName())
                .ipAddress(ipAddress)
                .category(request.getCategory() != null ? request.getCategory() : DeviceCategory.PERSONAL)
                .createdAt(LocalDateTime.now())
                .lastSeen(LocalDateTime.now())
                .build();

        Device saved = deviceRepository.save(device);
        log.info("Device created successfully with UUID: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public DeviceResponse updateDevice(UUID id, DeviceHeartbeatRequest request, String ipAddress) {
        log.info("Updating device with UUID: {}", id);

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        device.setMachineName(request.getMachineName());
        device.setUserName(request.getUserName());
        device.setIpAddress(ipAddress);
        device.setCategory(request.getCategory() != null ? request.getCategory() : device.getCategory());
        device.setLastSeen(LocalDateTime.now());

        Device updated = deviceRepository.save(device);
        log.info("Device updated successfully with UUID: {}", updated.getId());

        return mapToResponse(updated);
    }

    @Override
    public List<DeviceResponse> getAllDevices() {
        log.info("Fetching all devices from repository");
        return deviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DeviceResponse getDeviceById(UUID id) {
        log.info("Fetching device by UUID: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return mapToResponse(device);
    }

    private DeviceResponse mapToResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId().toString())
                .deviceIdentifier(device.getDeviceIdentifier())
                .machineName(device.getMachineName())
                .userName(device.getUserName())
                .ipAddress(device.getIpAddress())
                .category(device.getCategory().name())
                .lastSeen(device.getLastSeen())
                .build();
    }
}

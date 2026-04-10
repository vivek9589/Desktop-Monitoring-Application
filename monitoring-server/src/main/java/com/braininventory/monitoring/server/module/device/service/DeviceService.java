package com.braininventory.monitoring.server.module.device.service;




import com.braininventory.monitoring.server.module.device.dto.request.DeviceHeartbeatRequest;
import com.braininventory.monitoring.server.module.device.dto.response.DeviceResponse;

import java.util.List;
import java.util.UUID;

public interface DeviceService {

    // Create a new device record
    DeviceResponse createDevice(DeviceHeartbeatRequest request, String ipAddress);

    // Update an existing device record
    DeviceResponse updateDevice(UUID id, DeviceHeartbeatRequest request, String ipAddress);

    // Fetch all devices
    List<DeviceResponse> getAllDevices();

    // Fetch a single device by its UUID
    DeviceResponse getDeviceById(UUID id);
}


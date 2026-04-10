package com.braininventory.monitoring.server.module.device.dto.request;


import com.braininventory.monitoring.server.module.device.DeviceCategory;
import lombok.Data;

@Data
public class DeviceHeartbeatRequest {
    private String deviceIdentifier;   // external identifier sent by device
    private String machineName;
    private String userName;
    private DeviceCategory category;
}


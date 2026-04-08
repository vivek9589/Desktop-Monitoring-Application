package com.braininventory.monitoring.screenshot.monitor.agent.module.device.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeviceResponse {
    private String id;
    private String deviceIdentifier;
    private String machineName;
    private String userName;
    private String ipAddress;
    private String category;
    private LocalDateTime lastSeen;
}


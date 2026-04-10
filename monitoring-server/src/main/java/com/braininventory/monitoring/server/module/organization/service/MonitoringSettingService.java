package com.braininventory.monitoring.server.module.organization.service;


import com.braininventory.monitoring.server.module.organization.dto.request.MonitoringSettingRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.MonitoringSettingResponseDto;

import java.util.UUID;

public interface MonitoringSettingService {
    MonitoringSettingResponseDto createOrUpdate(UUID organizationId, MonitoringSettingRequestDto requestDto);
    MonitoringSettingResponseDto getByOrganizationId(UUID organizationId);
    void delete(UUID organizationId);
}


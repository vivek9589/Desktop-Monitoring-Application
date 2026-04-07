package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request.MonitoringSettingRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.response.MonitoringSettingResponseDto;

import java.util.UUID;

public interface MonitoringSettingService {
    MonitoringSettingResponseDto createOrUpdate(UUID organizationId, MonitoringSettingRequestDto requestDto);
    MonitoringSettingResponseDto getByOrganizationId(UUID organizationId);
    void delete(UUID organizationId);
}


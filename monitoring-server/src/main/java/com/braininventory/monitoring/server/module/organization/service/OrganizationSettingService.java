package com.braininventory.monitoring.server.module.organization.service;


import com.braininventory.monitoring.server.module.organization.dto.request.OrganizationSettingRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.OrganizationSettingResponseDto;

import java.util.UUID;

public interface OrganizationSettingService {
    OrganizationSettingResponseDto create(UUID organizationId, OrganizationSettingRequestDto requestDto);
    OrganizationSettingResponseDto update(UUID organizationId, OrganizationSettingRequestDto requestDto);
    OrganizationSettingResponseDto getByOrganizationId(UUID organizationId);
    void delete(UUID organizationId);
}


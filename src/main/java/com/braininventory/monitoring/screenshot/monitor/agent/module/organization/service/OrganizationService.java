package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service;

import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request.OrganizationRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.response.OrganizationResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    OrganizationResponseDto createOrganization(OrganizationRequestDto requestDto);
    OrganizationResponseDto getOrganizationById(UUID id);
    List<OrganizationResponseDto> getAllOrganizations();
    OrganizationResponseDto updateOrganization(UUID id, OrganizationRequestDto requestDto);
    void deleteOrganization(UUID id);
}

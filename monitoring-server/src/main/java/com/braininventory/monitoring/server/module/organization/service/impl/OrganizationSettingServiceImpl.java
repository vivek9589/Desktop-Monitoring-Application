package com.braininventory.monitoring.server.module.organization.service.impl;


import com.braininventory.monitoring.common.exception.OrganizationNotFoundException;
import com.braininventory.monitoring.server.module.organization.dto.request.OrganizationSettingRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.OrganizationSettingResponseDto;
import com.braininventory.monitoring.server.module.organization.entity.Organization;
import com.braininventory.monitoring.server.module.organization.entity.OrganizationSetting;
import com.braininventory.monitoring.server.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.server.module.organization.repository.OrganizationSettingRepository;
import com.braininventory.monitoring.server.module.organization.service.OrganizationSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrganizationSettingServiceImpl implements OrganizationSettingService {

    private final OrganizationSettingRepository settingRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationSettingResponseDto create(UUID organizationId, OrganizationSettingRequestDto requestDto) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        if (settingRepository.existsByOrganization_Id(organizationId)) {
            throw new IllegalStateException("Settings already exist for organization " + organizationId);
        }

        OrganizationSetting setting = OrganizationSetting.builder()
                .organization(organization)
                .workingHoursPerDay(requestDto.getWorkingHoursPerDay())
                .workingDaysPerWeek(requestDto.getWorkingDaysPerWeek())
                .timezone(requestDto.getTimezone())
                .build();

        OrganizationSetting saved = settingRepository.save(setting);
        log.info("Created settings for organization {}", organizationId);
        return mapToResponseDto(saved);
    }

    @Override
    public OrganizationSettingResponseDto update(UUID organizationId, OrganizationSettingRequestDto requestDto) {
        OrganizationSetting setting = settingRepository.findByOrganization_Id(organizationId)
                .orElseThrow(() -> new RuntimeException("Settings not found for organization " + organizationId));

        setting.setWorkingHoursPerDay(requestDto.getWorkingHoursPerDay());
        setting.setWorkingDaysPerWeek(requestDto.getWorkingDaysPerWeek());
        setting.setTimezone(requestDto.getTimezone());

        OrganizationSetting saved = settingRepository.save(setting);
        log.info("Updated settings for organization {}", organizationId);
        return mapToResponseDto(saved);
    }

    @Override
    public OrganizationSettingResponseDto getByOrganizationId(UUID organizationId) {
        OrganizationSetting setting = settingRepository.findByOrganization_Id(organizationId)
                .orElseThrow(() -> new RuntimeException("Settings not found for organization " + organizationId));
        log.info("Fetched settings for organization {}", organizationId);
        return mapToResponseDto(setting);
    }

    @Override
    public void delete(UUID organizationId) {
        if (!settingRepository.existsByOrganization_Id(organizationId)) {
            throw new RuntimeException("Settings not found for organization " + organizationId);
        }
        settingRepository.deleteByOrganization_Id(organizationId);
        log.info("Deleted settings for organization {}", organizationId);
    }

    private OrganizationSettingResponseDto mapToResponseDto(OrganizationSetting setting) {
        return OrganizationSettingResponseDto.builder()
                .organizationId(setting.getOrganization().getId())
                .workingHoursPerDay(setting.getWorkingHoursPerDay())
                .workingDaysPerWeek(setting.getWorkingDaysPerWeek())
                .timezone(setting.getTimezone())
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}


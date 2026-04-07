package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.OrganizationNotFoundException;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request.MonitoringSettingRequestDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.response.MonitoringSettingResponseDto;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.MonitoringSetting;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.entity.Organization;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository.MonitoringSettingRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.organization.service.MonitoringSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MonitoringSettingServiceImpl implements MonitoringSettingService {

    private final MonitoringSettingRepository monitoringSettingRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public MonitoringSettingResponseDto createOrUpdate(UUID organizationId, MonitoringSettingRequestDto requestDto) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        MonitoringSetting setting = monitoringSettingRepository.findByOrganization_Id(organizationId)
                .orElseGet(() -> MonitoringSetting.builder()
                        .organization(organization)
                        .build());

        setting.setScreenshotFrequencyMinutes(requestDto.getScreenshotFrequencyMinutes());
        setting.setScreenshotStoragePath(requestDto.getScreenshotStoragePath());
        setting.setScreenshotUploadPath(requestDto.getScreenshotUploadPath());
        setting.setOsType(requestDto.getOsType());
        setting.setAgentAppIntervalMs(requestDto.getAgentAppIntervalMs());
        setting.setWebsiteApiPath(requestDto.getWebsiteApiPath());
        setting.setWebsiteIntervalMs(requestDto.getWebsiteIntervalMs());
        setting.setActivityRateMs(requestDto.getActivityRateMs());
        setting.setIdleCheckRateMs(requestDto.getIdleCheckRateMs());
        setting.setBlurScreenshots(requestDto.getBlurScreenshots());

        MonitoringSetting saved = monitoringSettingRepository.save(setting);
        log.info("Saved monitoring settings for organization {}", organizationId);
        return mapToResponseDto(saved);
    }

    @Override
    public MonitoringSettingResponseDto getByOrganizationId(UUID organizationId) {
        MonitoringSetting setting = monitoringSettingRepository.findByOrganization_Id(organizationId)
                .orElseThrow(() -> new RuntimeException("Monitoring settings not found for organization " + organizationId));
        return mapToResponseDto(setting);
    }

    @Override
    public void delete(UUID organizationId) {
        if (!monitoringSettingRepository.existsByOrganization_Id(organizationId)) {
            throw new RuntimeException("Monitoring settings not found for organization " + organizationId);
        }
        monitoringSettingRepository.deleteByOrganization_Id(organizationId);
        log.info("Deleted monitoring settings for organization {}", organizationId);
    }

    private MonitoringSettingResponseDto mapToResponseDto(MonitoringSetting setting) {
        return MonitoringSettingResponseDto.builder()
                .organizationId(setting.getOrganization().getId())
                .screenshotFrequencyMinutes(setting.getScreenshotFrequencyMinutes())
                .screenshotStoragePath(setting.getScreenshotStoragePath())
                .screenshotUploadPath(setting.getScreenshotUploadPath())
                .osType(setting.getOsType())
                .agentAppIntervalMs(setting.getAgentAppIntervalMs())
                .websiteApiPath(setting.getWebsiteApiPath())
                .websiteIntervalMs(setting.getWebsiteIntervalMs())
                .activityRateMs(setting.getActivityRateMs())
                .idleCheckRateMs(setting.getIdleCheckRateMs())
                .blurScreenshots(setting.getBlurScreenshots())
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}



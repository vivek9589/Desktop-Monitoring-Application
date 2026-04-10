package com.braininventory.monitoring.server.module.organization.dto.response;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringSettingResponseDto {
    private UUID organizationId;
    private Integer screenshotFrequencyMinutes;
    private String screenshotStoragePath;
    private String screenshotUploadPath;
    private String osType;
    private Integer agentAppIntervalMs;
    private String websiteApiPath;
    private Integer websiteIntervalMs;
    private Integer activityRateMs;
    private Integer idleCheckRateMs;
    private Boolean blurScreenshots;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringSettingRequestDto {
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
}


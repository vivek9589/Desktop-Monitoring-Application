package com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for transferring website usage data between agent and backend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteUsageDto {

    private String url;
    private String title;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;            // in seconds
}
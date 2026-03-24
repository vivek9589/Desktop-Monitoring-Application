package com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long startTime;  // epoch millis
    private Long endTime;
    private Long duration;
}
package com.braininventory.monitoring.common.dto.request;



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

    private String agentId;
    private String url;
    private String title;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationSeconds;            // in seconds //  private Long durationSeconds;    // ✅ rename for consistency with entity


}
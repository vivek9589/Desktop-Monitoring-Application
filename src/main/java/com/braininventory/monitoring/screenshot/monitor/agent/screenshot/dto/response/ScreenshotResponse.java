package com.braininventory.monitoring.screenshot.monitor.agent.screenshot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreenshotResponse {
    private Long id;
    private String agentId;
    private String fileUrl;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}

package com.braininventory.monitoring.server.module.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScreenshotDTO {
    private String url;
    private String userId;
    private LocalDateTime timestamp;
}

package com.braininventory.monitoring.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenshotUploadRequest {
    private String agentId;
    private String timestamp;
}
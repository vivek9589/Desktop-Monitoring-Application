package com.braininventory.monitoring.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityResponse {
    private String agentId;
    private String status;
    private LocalDateTime timestamp;
}

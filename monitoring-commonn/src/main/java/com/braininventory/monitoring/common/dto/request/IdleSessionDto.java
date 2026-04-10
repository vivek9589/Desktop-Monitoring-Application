package com.braininventory.monitoring.common.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IdleSessionDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long durationSeconds;
}
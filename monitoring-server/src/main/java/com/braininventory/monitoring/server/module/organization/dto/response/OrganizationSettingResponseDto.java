package com.braininventory.monitoring.server.module.organization.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSettingResponseDto {
    private UUID organizationId;
    private Integer workingHoursPerDay;
    private Integer workingDaysPerWeek;
    private String timezone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


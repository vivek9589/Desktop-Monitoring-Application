package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSettingRequestDto {
    @NotNull
    private Integer workingHoursPerDay;

    @NotNull
    private Integer workingDaysPerWeek;

    private String timezone;
}


package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequestDto {
    @NotBlank
    private String name;

    private String timezone = "IST";
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String contactEmail;
}


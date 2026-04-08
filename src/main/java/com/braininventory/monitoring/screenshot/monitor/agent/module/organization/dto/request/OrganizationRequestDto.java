package com.braininventory.monitoring.screenshot.monitor.agent.module.organization.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    private String timezone = "Asia/Kolkata";

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @Size(max = 50)
    private String phoneNumber;

    @Email
    private String contactEmail;
}

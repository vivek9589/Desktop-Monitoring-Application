package com.braininventory.monitoring.server.module.auth.dto.request;

import com.braininventory.monitoring.server.module.organization.dto.request.OrganizationRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OnboardingRequest {
    @Valid
    @NotNull(message = "Admin details are required")
    private RegisterRequest adminDetails;

    @Valid
    @NotNull(message = "Organization details are required")
    private OrganizationRequestDto organizationDetails;
}
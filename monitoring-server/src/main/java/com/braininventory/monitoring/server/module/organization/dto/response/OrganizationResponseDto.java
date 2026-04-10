package com.braininventory.monitoring.server.module.organization.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponseDto {

    private UUID id;
    private String name;
    private String timezone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String contactEmail;

    private boolean isActive;

    private LocalDateTime createdAt;  // added audit field
    private LocalDateTime updatedAt;  // added audit field
}

package com.braininventory.monitoring.server.module.user.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private UUID organizationId;
    private boolean isActive;
    private LocalDateTime createdAt;
}


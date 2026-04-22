package com.braininventory.monitoring.server.module.project.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToProjectRequest {
    private UUID projectId;
    private List<UUID> userIds;
}


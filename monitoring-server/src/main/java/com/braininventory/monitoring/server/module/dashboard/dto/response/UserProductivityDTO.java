package com.braininventory.monitoring.server.module.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProductivityDTO {
    private String userId;
    private String name;
    private long activeTime;
    private long idleTime;
    private double productivity;
}
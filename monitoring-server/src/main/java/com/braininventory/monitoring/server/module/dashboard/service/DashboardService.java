package com.braininventory.monitoring.server.module.dashboard.service;

import com.braininventory.monitoring.server.module.dashboard.dto.response.AdminDashboardResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public interface DashboardService {

    AdminDashboardResponseDTO getAdminDashboard(
            UUID organizationId,
            LocalDate start,
            LocalDate end
    );
}

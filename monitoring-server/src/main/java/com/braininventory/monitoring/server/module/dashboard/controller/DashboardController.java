package com.braininventory.monitoring.server.module.dashboard.controller;


import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.dashboard.dto.response.AdminDashboardResponseDTO;
import com.braininventory.monitoring.server.module.dashboard.dto.response.UserDashboardResponseDTO;
import com.braininventory.monitoring.server.module.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Admin Dashboard API
     *
     * Example:
     * GET /api/dashboard/admin?organizationId=uuid&start=2026-04-01&end=2026-04-30
     */
    @GetMapping("/admin")
    public ApiResponse<AdminDashboardResponseDTO> getAdminDashboard(
            @RequestParam UUID organizationId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end
    ) {

        String requestId = UUID.randomUUID().toString();
        log.info("Admin dashboard request received. requestId={}, orgId={}", requestId, organizationId);

        try {

            // Default date handling (optional but useful)
            if (start == null) start = LocalDate.now();
            if (end == null) end = LocalDate.now();

            AdminDashboardResponseDTO response =
                    dashboardService.getAdminDashboard(organizationId, start, end);

            return ApiResponse.success(response, requestId);

        } catch (Exception ex) {

            log.error("Error generating dashboard. requestId={}", requestId, ex);

            return ApiResponse.error(
                    "DASHBOARD_ERROR",
                    "Failed to fetch admin dashboard",
                    ex.getMessage(),
                    requestId
            );
        }
    }


    @GetMapping("/user/{userId}")
    public ApiResponse<UserDashboardResponseDTO> getUserDashboard(

            @PathVariable String userId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end
    ) {

        String requestId = UUID.randomUUID().toString();

        try {

            if (start == null) start = LocalDate.now();
            if (end == null) end = LocalDate.now();

            UserDashboardResponseDTO response =
                    dashboardService.getUserDashboard(userId, start, end);

            return ApiResponse.success(response, requestId);

        } catch (Exception ex) {

            return ApiResponse.error(
                    "USER_DASHBOARD_ERROR",
                    "Failed to fetch user dashboard",
                    ex.getMessage(),
                    requestId
            );
        }
    }
}

package com.braininventory.monitoring.server.module.dashboard.service.impl;

import com.braininventory.monitoring.common.dto.response.ProductivityReportDto;
import com.braininventory.monitoring.common.dto.response.ScreenshotResponse;
import com.braininventory.monitoring.server.module.activitybackend.service.AppUsageService;
import com.braininventory.monitoring.server.module.activitybackend.service.ScreenshotService;
import com.braininventory.monitoring.server.module.activitybackend.service.WebsiteService;
import com.braininventory.monitoring.server.module.dashboard.dto.response.*;
import com.braininventory.monitoring.server.module.dashboard.service.DashboardService;
import com.braininventory.monitoring.server.module.productivity.service.ProductivityService;
import com.braininventory.monitoring.server.module.project.dto.response.ProjectResponse;
import com.braininventory.monitoring.server.module.project.service.ProjectService;
import com.braininventory.monitoring.server.module.user.dto.request.UserResponse;
import com.braininventory.monitoring.server.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final UserService userService;
    private final ProductivityService productivityService;
    private final ProjectService projectService;
    private final ScreenshotService screenshotService;

    private final AppUsageService appUsageService;
    private final WebsiteService webUsageService;

    @Override
    public AdminDashboardResponseDTO getAdminDashboard(
            UUID organizationId,
            LocalDate start,
            LocalDate end
    ) {

        log.info("Generating admin dashboard for orgId={}", organizationId);

        try {

            // =========================
            // 1. USERS
            // =========================
            List<UserResponse> users =
                    userService.getActiveEmployeesByOrganization(organizationId);

            int totalEmployees = users.size();

            // ✅ extract agentIds (IMPORTANT)
            List<String> agentIds = users.stream()
                    .map(u -> u.getId().toString())
                    .toList();

            // =========================
            // 2. PROJECTS
            // =========================
            int totalProjects = (int) projectService.getAll().stream()
                    .filter(p -> organizationId.equals(p.getOrganizationId()))
                    .count();

            // =========================
            // 3. PRODUCTIVITY
            // =========================
            List<UserProductivityDTO> productivityList = new ArrayList<>();

            for (UserResponse user : users) {

                ProductivityReportDto report =
                        productivityService.getReportByDateRange(
                                user.getId().toString(),
                                start,
                                end
                        );

                long idle = report.getIdleTime();
                long productive =
                        report.getProductiveAppTime() + report.getProductiveWebTime();

                long total = idle + productive;

                double productivity = total == 0 ? 0 :
                        (double) productive / total;

                productivityList.add(
                        UserProductivityDTO.builder()
                                .userId(user.getId().toString())
                                .name(user.getName())
                                .activeTime(productive)
                                .idleTime(idle)
                                .productivity(productivity)
                                .build()
                );
            }

            int activeUsers = (int) productivityList.stream()
                    .filter(u -> u.getActiveTime() > 0)
                    .count();

            // =========================
            // 4. TOP APPS + WEBSITES (NEW)
            // =========================
            List<AppUsageDTO> topApps =
                    appUsageService.getTopAppsByAgents(agentIds, start, end);

            List<WebsiteUsageDTO> topWebsites =
                    webUsageService.getTopWebsitesByAgents(agentIds, start, end);

            // =========================
// 5. TREND (ORG LEVEL)
// =========================
            List<TrendDTO> trend = new ArrayList<>();

            LocalDate current = start;

            while (!current.isAfter(end)) {

                double totalScore = 0;
                int count = 0;

                for (String agentId : agentIds) {

                    ProductivityReportDto report =
                            productivityService.getReportByDateRange(
                                    agentId,
                                    current,
                                    current
                            );

                    totalScore += report.getProductivityScore();
                    count++;
                }

                double avg = count == 0 ? 0 : totalScore / count;

                trend.add(
                        TrendDTO.builder()
                                .date(current.toString())
                                .avgProductivity(avg)
                                .build()
                );

                current = current.plusDays(1);
            }

            // =========================
            // 6. SCREENSHOTS
            // =========================
            List<ScreenshotDTO> screenshots = new ArrayList<>();

            for (UserResponse user : users) {

                Page<ScreenshotResponse> page =
                        screenshotService.getScreenshots(
                                user.getId().toString(),
                                null,
                                null,
                                PageRequest.of(0, 20)
                        );

                List<ScreenshotDTO> mappedScreenshots =
                        page.getContent().stream()
                                .map(s -> {
                                    try {
                                        LocalDateTime ts =
                                                LocalDateTime.parse(s.getTimestamp());

                                        return ScreenshotDTO.builder()
                                                .url(s.getFileUrl())
                                                .userId(s.getAgentId())
                                                .timestamp(ts)
                                                .build();

                                    } catch (Exception e) {
                                        log.warn("Invalid timestamp for screenshot id={}", s.getId());
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .filter(s ->
                                        (start == null || !s.getTimestamp().toLocalDate().isBefore(start)) &&
                                                (end == null || !s.getTimestamp().toLocalDate().isAfter(end))
                                )
                                .toList();

                screenshots.addAll(mappedScreenshots);
            }

            List<ScreenshotDTO> finalScreenshots =
                    screenshots.stream()
                            .sorted(Comparator.comparing(ScreenshotDTO::getTimestamp).reversed())
                            .limit(5)
                            .toList();

            // =========================
            // 7. SUMMARY
            // =========================
            SummaryDTO summary = SummaryDTO.builder()
                    .totalEmployees(totalEmployees)
                    .activeUsers(activeUsers)
                    .totalProjects(totalProjects)
                    .build();

            // =========================
            // FINAL RESPONSE
            // =========================
            return AdminDashboardResponseDTO.builder()
                    .summary(summary)
                    .productivity(productivityList)
                    .trend(trend)                 // ✅ now filled
                    .topApps(topApps)             // ✅ now filled
                    .topWebsites(topWebsites)     // ✅ now filled
                    .screenshots(finalScreenshots)
                    .build();

        } catch (Exception ex) {
            log.error("Dashboard generation failed for orgId={}", organizationId, ex);
            throw new RuntimeException("Failed to generate dashboard");
        }
    }

    @Override
    public UserDashboardResponseDTO getUserDashboard(
            String userId,
            LocalDate start,
            LocalDate end
    ) {

        log.info("Generating USER dashboard for userId={}", userId);

        try {

            // =========================
            // 1. PRODUCTIVITY
            // =========================
            ProductivityReportDto report =
                    productivityService.getReportByDateRange(
                            userId,
                            start,
                            end
                    );

            long idle = report.getIdleTime();
            long productive =
                    report.getProductiveAppTime() + report.getProductiveWebTime();

            long total = idle + productive;

            double productivityScore = report.getProductivityScore();

            UserProductivityDTO productivity =
                    UserProductivityDTO.builder()
                            .userId(userId)
                            //.name("Self") // or fetch from userService if needed
                            .activeTime(productive)
                            .idleTime(idle)
                            .productivity(productivityScore)
                            .build();

            // =========================
            // 2. TOP APPS
            // =========================
            List<AppUsageDTO> topApps =
                    appUsageService.getTopAppsByAgents(
                            List.of(userId),
                            start,
                            end
                    );

            // =========================
            // 3. TOP WEBSITES
            // =========================
            List<WebsiteUsageDTO> topWebsites =
                    webUsageService.getTopWebsitesByAgents(
                            List.of(userId),
                            start,
                            end
                    );

            // =========================
            // 4. TREND (USER LEVEL)
            // =========================
            List<TrendDTO> trend = new ArrayList<>();

            LocalDate current = start;

            while (!current.isAfter(end)) {

                ProductivityReportDto dailyReport =
                        productivityService.getReportByDateRange(
                                userId,
                                current,
                                current
                        );

                trend.add(
                        TrendDTO.builder()
                                .date(current.toString())
                                .avgProductivity(dailyReport.getProductivityScore())
                                .build()
                );

                current = current.plusDays(1);
            }

            // =========================
            // 5. SCREENSHOTS
            // =========================
            Page<ScreenshotResponse> page =
                    screenshotService.getScreenshots(
                            userId,
                            null,
                            null,
                            PageRequest.of(0, 20)
                    );

            List<ScreenshotDTO> screenshots =
                    page.getContent().stream()
                            .map(s -> {
                                try {
                                    LocalDateTime ts =
                                            LocalDateTime.parse(s.getTimestamp());

                                    return ScreenshotDTO.builder()
                                            .url(s.getFileUrl())
                                            .userId(s.getAgentId())
                                            .timestamp(ts)
                                            .build();

                                } catch (Exception e) {
                                    log.warn("Invalid timestamp for screenshot id={}", s.getId());
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .filter(s ->
                                    (start == null || !s.getTimestamp().toLocalDate().isBefore(start)) &&
                                            (end == null || !s.getTimestamp().toLocalDate().isAfter(end))
                            )
                            .sorted(Comparator.comparing(ScreenshotDTO::getTimestamp).reversed())
                            .limit(5)
                            .toList();

            // =========================
            // FINAL RESPONSE
            // =========================
            return UserDashboardResponseDTO.builder()
                    .productivity(productivity)
                    .trend(trend)
                    .topApps(topApps)
                    .topWebsites(topWebsites)
                    .screenshots(screenshots)
                    .build();

        } catch (Exception ex) {
            log.error("User dashboard failed for userId={}", userId, ex);
            throw new RuntimeException("Failed to generate user dashboard");
        }
    }
}
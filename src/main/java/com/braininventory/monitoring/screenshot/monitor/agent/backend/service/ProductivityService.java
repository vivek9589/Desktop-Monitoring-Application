package com.braininventory.monitoring.screenshot.monitor.agent.backend.service;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ProductivityReportDto;

import java.time.LocalDate;
import java.util.List;

public interface ProductivityService {

    /**
     * Get today's productivity report for a given agent.
     *
     * @param agentId the agent identifier
     * @return ProductivityReportDto containing idle time, productive app/web time, and score
     */
    ProductivityReportDto getTodayReport(String agentId);

    /**
     * Get yesterday's productivity report for a given agent.
     *
     * @param agentId the agent identifier
     * @return ProductivityReportDto
     */
    ProductivityReportDto getYesterdayReport(String agentId);

    /**
     * Get weekly productivity report (current week: Monday–Sunday).
     *
     * @param agentId the agent identifier
     * @return ProductivityReportDto
     */
    ProductivityReportDto getWeeklyReport(String agentId);

    /**
     * Get productivity report for a custom date range.
     *
     * @param agentId the agent identifier
     * @param start   start date (inclusive)
     * @param end     end date (inclusive)
     * @return ProductivityReportDto
     */
    ProductivityReportDto getCustomReport(String agentId, LocalDate start, LocalDate end);

    /**
     * Get trend report (daily scores for a given range).
     *
     * @param agentId the agent identifier
     * @param start   start date
     * @param end     end date
     * @return List of ProductivityReportDto (one per day)
     */
    List<ProductivityReportDto> getTrendReport(String agentId, LocalDate start, LocalDate end);
}

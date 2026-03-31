package com.braininventory.monitoring.screenshot.monitor.agent.backend.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.ProductivityService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ProductivityReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productivity")
@RequiredArgsConstructor
public class ProductivityController {

    private final ProductivityService service;

    @GetMapping("/{agentId}/daily")
    public ResponseEntity<ApiResponse<ProductivityReportDto>> getToday(@PathVariable String agentId) {
        ProductivityReportDto data = service.getTodayReport(agentId);
        return ResponseEntity.ok(ApiResponse.success(data, generateId()));
    }

    @GetMapping("/{agentId}/yesterday")
    public ResponseEntity<ApiResponse<ProductivityReportDto>> getYesterday(@PathVariable String agentId) {
        ProductivityReportDto data = service.getYesterdayReport(agentId);
        return ResponseEntity.ok(ApiResponse.success(data, generateId()));
    }

    @GetMapping("/{agentId}/weekly")
    public ResponseEntity<ApiResponse<ProductivityReportDto>> getWeekly(@PathVariable String agentId) {
        ProductivityReportDto data = service.getWeeklyReport(agentId);
        return ResponseEntity.ok(ApiResponse.success(data, generateId()));
    }

    @GetMapping("/{agentId}/custom")
    public ResponseEntity<ApiResponse<ProductivityReportDto>> getCustom(
            @PathVariable String agentId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        ProductivityReportDto data = service.getCustomReport(agentId, start, end);
        return ResponseEntity.ok(ApiResponse.success(data, generateId()));
    }

    @GetMapping("/{agentId}/trend")
    public ResponseEntity<ApiResponse<List<ProductivityReportDto>>> getTrend(
            @PathVariable String agentId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        List<ProductivityReportDto> data = service.getTrendReport(agentId, start, end);
        return ResponseEntity.ok(ApiResponse.success(data, generateId()));
    }

    /**
     * Helper to generate a unique request ID for the Meta block.
     * In a production app, this is often handled by a Filter or Interceptor.
     */
    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
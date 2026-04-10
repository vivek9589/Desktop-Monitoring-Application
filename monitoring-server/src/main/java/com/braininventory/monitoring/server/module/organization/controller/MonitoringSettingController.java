package com.braininventory.monitoring.server.module.organization.controller;

import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.organization.dto.request.MonitoringSettingRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.MonitoringSettingResponseDto;
import com.braininventory.monitoring.server.module.organization.service.MonitoringSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{organizationId}/monitoring-settings")
@Slf4j
@RequiredArgsConstructor
public class MonitoringSettingController {

    private final MonitoringSettingService monitoringSettingService;

    @PostMapping
    public ResponseEntity<ApiResponse<MonitoringSettingResponseDto>> createOrUpdate(
            @PathVariable UUID organizationId,
            @Valid @RequestBody MonitoringSettingRequestDto requestDto,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        MonitoringSettingResponseDto response = monitoringSettingService.createOrUpdate(organizationId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MonitoringSettingResponseDto>> getByOrganizationId(
            @PathVariable UUID organizationId,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        MonitoringSettingResponseDto response = monitoringSettingService.getByOrganizationId(organizationId);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable UUID organizationId,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        monitoringSettingService.delete(organizationId);
        return ResponseEntity.ok(ApiResponse.success("Monitoring settings deleted successfully", requestId));
    }
}


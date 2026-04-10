package com.braininventory.monitoring.server.module.organization.controller;

import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.organization.dto.request.OrganizationSettingRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.OrganizationSettingResponseDto;
import com.braininventory.monitoring.server.module.organization.service.OrganizationSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{organizationId}/settings")
@Slf4j
@RequiredArgsConstructor
public class OrganizationSettingController {

    private final OrganizationSettingService settingService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationSettingResponseDto>> create(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationSettingRequestDto requestDto,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationSettingResponseDto response = settingService.create(organizationId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response, requestId));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrganizationSettingResponseDto>> update(
            @PathVariable UUID organizationId,
            @Valid @RequestBody OrganizationSettingRequestDto requestDto,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationSettingResponseDto response = settingService.update(organizationId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrganizationSettingResponseDto>> getByOrganizationId(
            @PathVariable UUID organizationId,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationSettingResponseDto response = settingService.getByOrganizationId(organizationId);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable UUID organizationId,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        settingService.delete(organizationId);
        return ResponseEntity.ok(ApiResponse.success("Organization settings deleted successfully", requestId));
    }
}


package com.braininventory.monitoring.server.module.organization.controller;
import com.braininventory.monitoring.common.dto.ApiResponse;
import com.braininventory.monitoring.server.module.organization.dto.request.OrganizationRequestDto;
import com.braininventory.monitoring.server.module.organization.dto.response.OrganizationResponseDto;
import com.braininventory.monitoring.server.module.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@Slf4j
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    // Create Organization
    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> createOrganization(
            @Valid @RequestBody OrganizationRequestDto requestDto,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationResponseDto response = organizationService.createOrganization(requestDto);
        log.debug("Created organization: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, requestId));
    }

    // Get Organization by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getOrganizationById(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationResponseDto response = organizationService.getOrganizationById(id);
        log.debug("Fetched organization with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    // Get All Organizations
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponseDto>>> getAllOrganizations(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        List<OrganizationResponseDto> responseList = organizationService.getAllOrganizations();
        log.debug("Fetched {} organizations", responseList.size());
        return ResponseEntity.ok(ApiResponse.success(responseList, requestId));
    }

    // Update Organization
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationRequestDto requestDto,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        OrganizationResponseDto response = organizationService.updateOrganization(id, requestDto);
        log.debug("Updated organization with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(response, requestId));
    }

    // Soft Delete Organization
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrganization(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId) {

        organizationService.deleteOrganization(id);
        log.debug("Soft deleted organization with id: {}", id);

        return ResponseEntity.ok(
                ApiResponse.success("Organization marked as inactive successfully", requestId)
        );
    }

}


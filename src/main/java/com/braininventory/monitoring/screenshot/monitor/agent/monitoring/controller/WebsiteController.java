package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for website usage tracking.
 * Handles incoming requests and delegates to service layer.
 */
@RestController
@RequestMapping("/api/website-usage")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;

    @PostMapping
    public ResponseEntity<ApiResponse<WebsiteUsageDto>> save(@RequestBody WebsiteUsageDto dto) {
        websiteService.save(dto);

        String requestId = UUID.randomUUID().toString();
        return ResponseEntity.ok(ApiResponse.success(dto, requestId));
    }
}
package com.braininventory.monitoring.screenshot.monitor.agent.backend.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.ScreenshotService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.response.ScreenshotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.UUID;


@RestController
@RequestMapping("/api/screenshots")
@RequiredArgsConstructor
@Slf4j
public class ScreenshotController {

    private final ScreenshotService screenshotService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ScreenshotResponse>> uploadScreenshot(
            @RequestPart("file") MultipartFile file,
            @RequestPart("agentId") String agentId,
            @RequestPart("timestamp") String timestamp) {

        String requestId = UUID.randomUUID().toString();
        try {
            ScreenshotUploadRequest request = new ScreenshotUploadRequest(agentId, timestamp);
            ScreenshotResponse response = screenshotService.uploadScreenshot(file, request);

            return ResponseEntity.ok(ApiResponse.success(response, requestId));
        } catch (Exception e) {
            log.error("Error uploading screenshot", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("UPLOAD_ERROR", "Failed to upload screenshot", e.getMessage(), requestId));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ScreenshotResponse>>> getScreenshots(
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String requestId = UUID.randomUUID().toString();
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
            Page<ScreenshotResponse> screenshots = screenshotService.getScreenshots(agentId, startDate, endDate, pageable);

            return ResponseEntity.ok(ApiResponse.success(screenshots, requestId));
        } catch (Exception e) {
            log.error("Error fetching screenshots", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("FETCH_ERROR", "Failed to fetch screenshots", e.getMessage(), requestId));
        }
    }
}
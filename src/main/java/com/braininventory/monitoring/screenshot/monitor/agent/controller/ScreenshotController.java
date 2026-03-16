package com.braininventory.monitoring.screenshot.monitor.agent.controller;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.ApiResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.ScreenshotResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.service.ScreenshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/screenshots")
public class ScreenshotController {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotController.class);

    @Autowired
    private ScreenshotService screenshotService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ScreenshotResponse>> uploadScreenshot(
            @RequestParam("file") MultipartFile file,
            @RequestParam("agentId") String agentId,
            @RequestParam("timestamp")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        try {
            ScreenshotUploadRequest request = new ScreenshotUploadRequest(agentId, timestamp);
            ScreenshotResponse response = screenshotService.uploadScreenshot(file, request);
            return ResponseEntity.ok(ApiResponse.success(response, "Screenshot uploaded successfully"));
        } catch (Exception e) {
            logger.error("Error uploading screenshot", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload screenshot"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ScreenshotResponse>>> getScreenshots(
            @RequestParam(required = false) String agentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
            Page<ScreenshotResponse> screenshots = screenshotService.getScreenshots(agentId, startDate, endDate, pageable);
            return ResponseEntity.ok(ApiResponse.success(screenshots, "Screenshots fetched successfully"));
        } catch (Exception e) {
            logger.error("Error fetching screenshots", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch screenshots"));
        }
    }
}
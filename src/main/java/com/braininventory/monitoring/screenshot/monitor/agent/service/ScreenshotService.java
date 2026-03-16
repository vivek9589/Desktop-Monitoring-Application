package com.braininventory.monitoring.screenshot.monitor.agent.service;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.ScreenshotResponse;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ScreenshotService {
    ScreenshotResponse uploadScreenshot(MultipartFile file, ScreenshotUploadRequest request);
    Page<ScreenshotResponse> getScreenshots(String agentId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
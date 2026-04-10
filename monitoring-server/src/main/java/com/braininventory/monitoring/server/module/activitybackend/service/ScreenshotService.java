package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.common.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.common.dto.response.ScreenshotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface ScreenshotService {
    ScreenshotResponse uploadScreenshot(MultipartFile file, ScreenshotUploadRequest request);
    Page<ScreenshotResponse> getScreenshots(String agentId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
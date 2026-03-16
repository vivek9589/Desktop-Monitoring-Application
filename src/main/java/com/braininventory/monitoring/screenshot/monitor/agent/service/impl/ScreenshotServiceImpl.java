package com.braininventory.monitoring.screenshot.monitor.agent.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.screenshot.monitor.agent.dto.response.ScreenshotResponse;
import com.braininventory.monitoring.screenshot.monitor.agent.entity.Screenshot;
import com.braininventory.monitoring.screenshot.monitor.agent.repository.ScreenshotRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.service.CloudinaryService;
import com.braininventory.monitoring.screenshot.monitor.agent.service.ScreenshotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ScreenshotServiceImpl implements ScreenshotService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotServiceImpl.class);

    @Autowired
    private ScreenshotRepository screenshotRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public ScreenshotResponse uploadScreenshot(MultipartFile file, ScreenshotUploadRequest request) {
        try {
            logger.info("Uploading screenshot for agent: {}", request.getAgentId());

            // Upload to Cloudinary
            String fileUrl = cloudinaryService.uploadFile(file);

            // Save metadata in DB
            Screenshot screenshot = Screenshot.builder()
                    .agentId(request.getAgentId())
                    .filePath(fileUrl)
                    .timestamp(request.getTimestamp())
                    .createdAt(LocalDateTime.now())
                    .build();

            Screenshot saved = screenshotRepository.save(screenshot);

            return ScreenshotResponse.builder()
                    .id(saved.getId())
                    .agentId(saved.getAgentId())
                    .fileUrl(saved.getFilePath())
                    .timestamp(saved.getTimestamp())
                    .createdAt(saved.getCreatedAt())
                    .build();

        } catch (Exception e) {
            logger.error("Error uploading screenshot", e);
            throw new RuntimeException("Screenshot upload failed");
        }
    }

    @Override
    public Page<ScreenshotResponse> getScreenshots(String agentId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        logger.info("Fetching screenshots for agent: {}", agentId);

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();

        Page<Screenshot> screenshots = screenshotRepository.findByAgentIdAndTimestampBetween(agentId, start, end, pageable);

        return screenshots.map(s -> ScreenshotResponse.builder()
                .id(s.getId())
                .agentId(s.getAgentId())
                .fileUrl(s.getFilePath())
                .timestamp(s.getTimestamp())
                .createdAt(s.getCreatedAt())
                .build());
    }
}
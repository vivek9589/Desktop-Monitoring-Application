package com.braininventory.monitoring.server.module.activitybackend.service.impl;

import com.braininventory.monitoring.common.dto.request.ScreenshotUploadRequest;
import com.braininventory.monitoring.common.dto.response.ScreenshotResponse;
import com.braininventory.monitoring.common.exception.ScreenshotUploadException;
import com.braininventory.monitoring.server.module.activitybackend.entity.Screenshot;
import com.braininventory.monitoring.server.module.activitybackend.repository.ScreenshotRepository;
import com.braininventory.monitoring.server.module.activitybackend.service.CloudinaryService;
import com.braininventory.monitoring.server.module.activitybackend.service.ScreenshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    private final ScreenshotRepository screenshotRepository;
    private final CloudinaryService cloudinaryService;

    /**
     * Uploads a screenshot file to Cloudinary and saves metadata in DB.
     *
     * @param file    the screenshot file
     * @param request metadata about the screenshot (agentId, timestamp)
     * @return ScreenshotResponse containing saved screenshot details
     */
    @Override
    public ScreenshotResponse uploadScreenshot(MultipartFile file, ScreenshotUploadRequest request) {
        String agentId = request.getAgentId();
        log.info("Uploading screenshot for agent: {}", agentId);

        try {
            // Step 1: Upload file to Cloudinary
            String fileUrl = cloudinaryService.uploadFile(file);
            log.debug("Screenshot uploaded to Cloudinary. URL={}", fileUrl);

            // Step 2: Save metadata in DB
            Screenshot screenshot = Screenshot.builder()
                    .agentId(agentId)
                    .filePath(fileUrl)
                    .timestamp(request.getTimestamp())
                    .createdAt(LocalDateTime.now())
                    .build();

            Screenshot saved = screenshotRepository.save(screenshot);
            log.info("Screenshot saved in DB with id={} for agent={}", saved.getId(), agentId);

            // Step 3: Build response DTO
            return ScreenshotResponse.builder()
                    .id(saved.getId())
                    .agentId(saved.getAgentId())
                    .fileUrl(saved.getFilePath())
                    .timestamp(saved.getTimestamp())
                    .createdAt(saved.getCreatedAt())
                    .build();

        } catch (Exception e) {
            log.error("Error uploading screenshot for agent={}", agentId, e);
            throw new ScreenshotUploadException("Screenshot upload failed for agent " + agentId, e);
        }
    }

    /**
     * Fetches screenshots for a given agent within a date range.
     *
     * @param agentId   agent identifier
     * @param startDate optional start date
     * @param endDate   optional end date
     * @param pageable  pagination info
     * @return Page of ScreenshotResponse
     */
    @Override
    public Page<ScreenshotResponse> getScreenshots(String agentId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("Fetching screenshots for agent: {}", agentId);

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
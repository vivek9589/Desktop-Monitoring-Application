package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.screenshot.monitor.agent.common.exception.AgentException;
import com.braininventory.monitoring.screenshot.monitor.agent.common.util.UrlUtils;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.WebsiteUsage;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.enums.WebsiteCategory;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository.WebsiteRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.WebsiteClassificationService;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service.WebsiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Service implementation for website usage persistence and classification.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository repository;
    private final WebsiteClassificationService classificationService;

    @Override
    public void save(WebsiteUsageDto dto) {
        try {
            String domain = normalizeDomain(UrlUtils.extractDomain(dto.getUrl()));
            WebsiteCategory category = classificationService.classify(domain);

            WebsiteUsage entity = new WebsiteUsage();
            entity.setUrl(dto.getUrl());
            entity.setTitle(dto.getTitle());
            entity.setDomain(domain);
            entity.setCategory(category);
            entity.setStartTime(convert(dto.getStartTime()));
            entity.setEndTime(convert(dto.getEndTime()));
            entity.setDuration(dto.getDuration());

            repository.save(entity);

            log.info("Saved website usage: domain={} category={}", domain, category);

        } catch (Exception ex) {
            log.error("Failed to save website usage", ex);
            throw new AgentException("Failed to save website usage: " + ex.getMessage());
        }
    }

    private LocalDateTime convert(Long epoch) {
        return Instant.ofEpochMilli(epoch)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private String normalizeDomain(String domain) {
        return domain == null ? "" : domain.toLowerCase().replace("www.", "");
    }
}
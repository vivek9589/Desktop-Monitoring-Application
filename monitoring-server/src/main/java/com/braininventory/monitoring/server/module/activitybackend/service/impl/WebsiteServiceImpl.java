package com.braininventory.monitoring.server.module.activitybackend.service.impl;

import com.braininventory.monitoring.common.dto.request.WebsiteUsageDto;
import com.braininventory.monitoring.common.exception.AgentException;
import com.braininventory.monitoring.server.module.activitybackend.entity.WebsiteUsage;
import com.braininventory.monitoring.server.module.activitybackend.enums.Category;
import com.braininventory.monitoring.server.module.activitybackend.repository.WebsiteRepository;
import com.braininventory.monitoring.server.module.activitybackend.service.WebsiteService;
import com.braininventory.monitoring.server.module.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for website usage persistence and classification.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebsiteServiceImpl implements WebsiteService {

    private final WebsiteRepository repository;
    private final WebsiteClassifierImpl classificationService;

    @Override
    public void save(WebsiteUsageDto dto) {
        try {
            String domain = normalizeDomain(UrlUtils.extractDomain(dto.getUrl()));
            Category category = classificationService.classify(domain);

            WebsiteUsage entity = new WebsiteUsage();
            entity.setAgentId(dto.getAgentId());
            entity.setUrl(dto.getUrl());
            entity.setTitle(dto.getTitle());
            entity.setDomain(domain);
            entity.setCategory(category);

            // Directly use LocalDateTime from DTO
            entity.setStartTime(dto.getStartTime());
            entity.setEndTime(dto.getEndTime());
            entity.setDurationSeconds(dto.getDurationSeconds()); //  in seconds

            repository.save(entity);

            log.info("Saved website usage: domain={} category={}", domain, category);

        } catch (Exception ex) {
            log.error("Failed to save website usage", ex);
            throw new AgentException("Failed to save website usage: " + ex.getMessage());
        }
    }

    private String normalizeDomain(String domain) {
        return domain == null ? "" : domain.toLowerCase().replace("www.", "");
    }
}
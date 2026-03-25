package com.braininventory.monitoring.screenshot.monitor.agent.monitoring.service;

import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.entity.WebsiteClassification;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.enums.WebsiteCategory;
import com.braininventory.monitoring.screenshot.monitor.agent.monitoring.repository.WebsiteClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for classifying websites into categories.
 */
@Service
@RequiredArgsConstructor
public class WebsiteClassificationService {

    private final WebsiteClassificationRepository repository;

    public WebsiteCategory classify(String domain) {
        return repository.findByDomain(domain)
                .map(WebsiteClassification::getCategory)
                .orElse(defaultCategory(domain));
    }

    private WebsiteCategory defaultCategory(String domain) {
        if (domain.contains("github") || domain.contains("stackoverflow")) {
            return WebsiteCategory.PRODUCTIVE;
        }
        if (domain.contains("youtube") || domain.contains("netflix")) {
            return WebsiteCategory.UNPRODUCTIVE;
        }
        return WebsiteCategory.NEUTRAL;
    }
}
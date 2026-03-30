package com.braininventory.monitoring.screenshot.monitor.agent.backend.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.WebsiteClassification;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.Category;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.repository.WebsiteClassificationRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.Classifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service for classifying websites into categories.
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Qualifier("websiteClassifier")
@RequiredArgsConstructor
public class WebsiteClassifierImpl implements Classifier<String> {

    private final WebsiteClassificationRepository repository;

    @Override
    public Category classify(String domain) {
        return repository.findByDomain(domain)
                .map(WebsiteClassification::getCategory)
                .orElse(defaultCategory(domain));
    }

    private Category defaultCategory(String domain) {
        String lower = domain.toLowerCase();
        if (lower.contains("github") || lower.contains("stackoverflow")) {
            return Category.PRODUCTIVE;
        }
        if (lower.contains("youtube") || lower.contains("netflix")) {
            return Category.UNPRODUCTIVE;
        }
        return Category.NEUTRAL;
    }
}
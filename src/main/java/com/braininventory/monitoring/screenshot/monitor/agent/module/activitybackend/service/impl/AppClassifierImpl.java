package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service.impl;


import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.entity.AppClassification;
import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.enums.Category;
import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.repository.AppClassificationRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service.Classifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("appClassifier")
@RequiredArgsConstructor
public class AppClassifierImpl implements Classifier<String> {

    private final AppClassificationRepository repository;

    @Override
    public Category classify(String appName) {
        return repository.findByAppName(appName)
                .map(AppClassification::getCategory)
                .orElse(defaultCategory(appName));
    }

    private Category defaultCategory(String appName) {
        String lower = appName.toLowerCase();
        if (lower.contains("intellij") || lower.contains("eclipse") || lower.contains("vs code")) {
            return Category.PRODUCTIVE;
        }
        if (lower.contains("spotify") || lower.contains("game")) {
            return Category.UNPRODUCTIVE;
        }
        return Category.NEUTRAL;
    }
}
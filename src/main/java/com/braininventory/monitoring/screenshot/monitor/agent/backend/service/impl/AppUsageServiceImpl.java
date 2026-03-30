package com.braininventory.monitoring.screenshot.monitor.agent.backend.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.backend.entity.AppUsageSession;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.repository.AppUsageRepository;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.AppUsageService;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.Classifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AppUsageServiceImpl implements AppUsageService {

    private final AppUsageRepository repository;
    private final Classifier<String> appClassifier;

    public AppUsageServiceImpl(AppUsageRepository repository,
                               @Qualifier("appClassifier") Classifier<String> appClassifier) {
        this.repository = repository;
        this.appClassifier = appClassifier;
    }

    @Override
    public void save(AppUsageSession session) {
        session.setCategory(appClassifier.classify(session.getAppName()));
        repository.save(session);
        log.info("Saved AppUsageSession for agentId={} and appName={} with category={}",
                session.getAgentId(), session.getAppName(), session.getCategory());
    }
}
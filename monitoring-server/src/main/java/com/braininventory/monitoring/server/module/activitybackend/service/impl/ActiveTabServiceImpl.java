package com.braininventory.monitoring.server.module.activitybackend.service.impl;

import com.braininventory.monitoring.common.dto.request.ActiveTabDto;

import com.braininventory.monitoring.server.module.activitybackend.service.ActiveTabService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveTabServiceImpl implements ActiveTabService {
    // Store URL per Agent to avoid data mixing
    private final Map<String, ActiveTabDto> store = new ConcurrentHashMap<>();

    @Override
    public void update(ActiveTabDto dto) {
        // For now, we use a default key. In prod, the extension should send an Agent ID.
        store.put("agent-001", dto);
    }

    public String getCurrentUrl() {
        return store.getOrDefault("agent-001", new ActiveTabDto()).getUrl();
    }

    public String getCurrentTitle() {
        return store.getOrDefault("agent-001", new ActiveTabDto()).getTitle();
    }
}
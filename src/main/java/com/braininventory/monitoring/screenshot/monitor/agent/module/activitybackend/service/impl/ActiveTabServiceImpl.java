package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service.ActiveTabService;
import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActiveTabDto;
import org.springframework.stereotype.Service;


@Service
public class ActiveTabServiceImpl implements ActiveTabService {

    private volatile String currentUrl;
    private volatile String currentTitle;


    @Override
    public void update(ActiveTabDto dto) {
        this.currentUrl = dto.getUrl();
        this.currentTitle = dto.getTitle();
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }
}

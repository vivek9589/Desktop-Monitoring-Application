package com.braininventory.monitoring.agent.website;

import com.braininventory.monitoring.common.dto.request.ActiveTabDto;

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

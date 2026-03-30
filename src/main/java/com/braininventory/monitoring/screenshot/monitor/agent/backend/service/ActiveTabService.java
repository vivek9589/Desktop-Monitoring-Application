package com.braininventory.monitoring.screenshot.monitor.agent.backend.service;


import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActiveTabDto;



public interface ActiveTabService {

    void update(ActiveTabDto dto);
    String getCurrentUrl();
    public String getCurrentTitle();


}
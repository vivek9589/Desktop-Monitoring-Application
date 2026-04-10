package com.braininventory.monitoring.agent.website;


import com.braininventory.monitoring.common.dto.request.ActiveTabDto;

public interface ActiveTabService {

    void update(ActiveTabDto dto);
    String getCurrentUrl();
    public String getCurrentTitle();


}
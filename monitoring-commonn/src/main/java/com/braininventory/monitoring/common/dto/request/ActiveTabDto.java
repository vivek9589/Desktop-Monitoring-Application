package com.braininventory.monitoring.common.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class ActiveTabDto {
    private String url;
    private String title;
    private long timestamp;

    public ActiveTabDto(String currentUrl, String currentTitle) {
        this.url = currentUrl;
        this.title = currentTitle;
    }
}

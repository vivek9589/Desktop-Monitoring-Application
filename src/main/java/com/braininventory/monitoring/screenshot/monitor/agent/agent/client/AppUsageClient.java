package com.braininventory.monitoring.screenshot.monitor.agent.agent.client;

import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.AppActivityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AppUsageClient {

    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private  String baseUrl;

    public String send(AppActivityRequest request) {

        return restTemplate.postForObject(
                baseUrl + "/api/app-usage",
                request,
                String.class
        );
    }
}

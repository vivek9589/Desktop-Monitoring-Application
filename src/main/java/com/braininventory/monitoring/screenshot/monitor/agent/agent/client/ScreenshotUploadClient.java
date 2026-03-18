package com.braininventory.monitoring.screenshot.monitor.agent.agent.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;


@Component
@Slf4j
public class ScreenshotUploadClient {

    private final RestTemplate restTemplate;

    @Value("${backend.api.screenshot-upload-url}")
    private String backendUrl;


    public ScreenshotUploadClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String upload(File file, String agentId) {

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));
            body.add("agentId", agentId);
            body.add("timestamp", LocalDateTime.now().toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            log.info("Uploading screenshot -> agentId={}, file={}", agentId, file.getName());

            ResponseEntity<String> response =
                    restTemplate.postForEntity(backendUrl, request, String.class);

            log.info("Upload success -> Status={}, Body={}",
                    response.getStatusCode(), response.getBody());

            return response.getBody();

        } catch (Exception e) {
            log.error("Screenshot upload failed for agentId={}, file={}",
                    agentId, file.getName(), e);
            return null;
        }
    }
}
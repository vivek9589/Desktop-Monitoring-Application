package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.common.exception.AgentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ScreenshotUploadClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl;
    private final String apiKey;

    public ScreenshotUploadClient(
            @Value("${app.base-url}") String baseUrl,
            @Value("${backend.api.screenshot-upload-path}") String path,
            @Value("${backend.api.key}") String apiKey
    ) {
        this.backendUrl = baseUrl + path;
        this.apiKey = apiKey;
    }

    public String upload(File file, String agentId) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));
            body.add("agentId", agentId);
            body.add("timestamp", LocalDateTime.now().toString());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(apiKey);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    backendUrl,
                    requestEntity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            log.error("Screenshot upload failed -> file='{}', agentId={}, backendUrl={}",
                    file.getAbsolutePath(), agentId, backendUrl, e);

            throw new AgentException("Screenshot upload failed: " + e.getMessage());
        }
    }
}
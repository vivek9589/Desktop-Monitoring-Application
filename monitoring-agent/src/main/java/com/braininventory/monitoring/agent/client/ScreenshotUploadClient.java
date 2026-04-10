package com.braininventory.monitoring.agent.client;

import com.braininventory.monitoring.common.exception.AgentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.time.LocalDateTime;
@Slf4j
@Service
public class ScreenshotUploadClient {

    private final WebClient webClient;
    private final String backendUrl;
    private final String apiKey;

    public ScreenshotUploadClient(
            WebClient.Builder builder,
            @Value("${app.base-url}") String baseUrl,
            @Value("${backend.api.screenshot-upload-path}") String path,
            @Value("${backend.api.key}") String apiKey
    ) {
        this.webClient = builder.build();
        this.backendUrl = baseUrl + path;
        this.apiKey = apiKey;
    }

    public String upload(File file, String agentId) {
        try {
//            log.info("Uploading screenshot file='{}' to '{}', agentId={}",
//                    file.getAbsolutePath(), backendUrl, agentId);

            return webClient.post()
                    .uri(backendUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", new FileSystemResource(file))
                            .with("agentId", agentId)
                            .with("timestamp", LocalDateTime.now().toString()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            log.error("Screenshot upload failed -> file='{}', agentId={}, backendUrl={}",
                    file.getAbsolutePath(), agentId, backendUrl, e);
            throw new AgentException("Screenshot upload failed: " + e.getMessage());
        }
    }
}
package com.braininventory.monitoring.agent.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    // Final URL built in the constructor or via @Value logic
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;
    private final TokenManager tokenManager;

    @Value("${app.base-url:http://localhost:9090}")
    private String baseUrl;

    @Value("${backend.login.path:/api/auth/login}")
    private String loginPath;

    public boolean authenticate(String email, String password) {
        // Construct the full URL dynamically
        String fullUrl = baseUrl + loginPath;

        try {
            log.info("Attempting login at: {}", fullUrl);

            Map<String, String> body = Map.of("email", email, "password", password);
            String requestBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                // Navigate to: data -> token per your backend structure
                String token = root.path("data").path("token").asText();

                if (token != null && !token.isBlank()) {
                    tokenManager.saveToken(token);
                    return true;
                }
            } else {
                log.error("Login failed. Status: {}, Body: {}", response.statusCode(), response.body());
            }
            return false;
        } catch (Exception e) {
            log.error("Error during authentication request to {}", fullUrl, e);
            return false;
        }
    }
}
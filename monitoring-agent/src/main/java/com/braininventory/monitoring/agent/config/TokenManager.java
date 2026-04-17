package com.braininventory.monitoring.agent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class TokenManager {
    // Hidden folder in User's home: C:\Users\Name\.monitoring-agent\token.dat
    private final Path tokenPath = Paths.get(System.getProperty("user.home"), ".monitoring-agent", "token.dat");

    public void saveToken(String token) throws IOException {
        Files.createDirectories(tokenPath.getParent());
        Files.writeString(tokenPath, token);
        log.info("Auth token saved locally.");
    }

    public String getToken() {
        try {
            return Files.readString(tokenPath);
        } catch (IOException e) {
            return null; // Not logged in
        }
    }

    public boolean isAuthenticated() {
        String token = getToken();
        return token != null && !token.isBlank();
    }
}

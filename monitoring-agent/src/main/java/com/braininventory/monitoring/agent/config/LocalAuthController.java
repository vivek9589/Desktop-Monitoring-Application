package com.braininventory.monitoring.agent.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
// Allow your local testing environment (or any local origin) to hit this
@CrossOrigin(origins = "*")
public class LocalAuthController {

    private final TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        if (token == null || token.isBlank()) {
            log.warn("Login attempt failed: No token provided in request body.");
            return ResponseEntity.badRequest().body("Token is missing");
        }

        try {
            log.info("Received login request. Attempting to save token...");
            tokenManager.saveToken(token);

            log.info("Successfully authenticated! Agent is now linked to user.");
            return ResponseEntity.ok("Agent successfully authenticated and linked.");

        } catch (IOException e) {
            log.error("Critical error: Could not write token to local storage.", e);
            return ResponseEntity.internalServerError().body("Local storage error: " + e.getMessage());
        }
    }

    /**
     * Optional: Helper endpoint to check current status via browser
     * http://localhost:9999/auth/status
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        boolean authenticated = tokenManager.isAuthenticated();
        return ResponseEntity.ok(authenticated ? "Authenticated" : "Not Authenticated");
    }
}

package com.braininventory.monitoring.agent.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
@Component
@RequiredArgsConstructor
public class AuthContext {
    private final TokenManager tokenManager;
    private final ObjectMapper mapper = new ObjectMapper();

    public String getUserId() {
        return getClaim("userId");
    }

    public String getOrganizationId() {
        return getClaim("organizationId");
    }

    private String getClaim(String claimName) {
        try {
            String token = tokenManager.getToken();
            if (token == null) return "unknown";

            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode node = mapper.readTree(payload);
            return node.has(claimName) ? node.get(claimName).asText() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
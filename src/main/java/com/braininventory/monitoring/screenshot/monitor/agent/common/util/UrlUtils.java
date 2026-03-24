package com.braininventory.monitoring.screenshot.monitor.agent.common.util;

import lombok.extern.slf4j.Slf4j;
import java.net.URI;

/**
 * Utility class for URL processing.
 */
@Slf4j
public class UrlUtils {

    public static String extractDomain(String urlOrTitle) {
        if (urlOrTitle == null || urlOrTitle.isBlank()) return "unknown";

        try {
            if (urlOrTitle.startsWith("http")) {
                URI uri = new URI(urlOrTitle);
                String host = uri.getHost();
                if (host == null) return "unknown";
                return normalize(host.toLowerCase());
            }
            return extractFromTitle(urlOrTitle);
        } catch (Exception ex) {
            log.warn("Failed to extract domain from: {}", urlOrTitle);
            return "unknown";
        }
    }

    private static String normalize(String host) {
        return host.startsWith("www.") ? host.substring(4) : host;
    }

    private static String extractFromTitle(String title) {
        title = title.toLowerCase();
        if (title.contains("youtube")) return "youtube.com";
        if (title.contains("gmail")) return "gmail.com";
        if (title.contains("google")) return "google.com";
        if (title.contains("linkedin")) return "linkedin.com";
        if (title.contains("github")) return "github.com";
        if (title.contains("slack")) return "slack.com";
        if (title.contains("teams")) return "teams.microsoft.com";
        if (title.contains("jira")) return "jira.com";
        return title;
    }
}
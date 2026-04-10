package com.braininventory.monitoring.agent.website.detector;

/**
 * Immutable record holding browser metadata.
 */
public record BrowserMetadata(
        String app,
        String title,
        String url,
        boolean isBrowser
) {}
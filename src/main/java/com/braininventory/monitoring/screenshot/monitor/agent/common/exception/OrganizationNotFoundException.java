package com.braininventory.monitoring.screenshot.monitor.agent.common.exception;

import java.util.UUID;

public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(UUID id) {
        super("Organization not found with id: " + id.toString());
    }

    public OrganizationNotFoundException(String message) {
        super(message);
    }
}

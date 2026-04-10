package com.braininventory.monitoring.common.exception;

import java.util.UUID;

public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(UUID id) {
        super("Organization not found with id: " + id.toString());
    }

    public OrganizationNotFoundException(String message) {
        super(message);
    }
}

package com.braininventory.monitoring.common.exception;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(String message) {
        super(message);
    }
}

package com.braininventory.monitoring.server.module.project.enums;


/**
 * Enum representing the lifecycle status of a Task.
 */
public enum TaskStatus {
    PENDING,        // Task created but not started
    IN_PROGRESS,    // Task currently being worked on
    COMPLETED,      // Task finished successfully
    BLOCKED,        // Task cannot proceed due to dependency/issue
    CANCELLED       // Task was cancelled
}
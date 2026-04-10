package com.braininventory.monitoring.agent.apptracking.detector;



/**
 * Provides active window and application details.
 */
public interface ActiveWindowDetector {

    String getActiveApp();

    String getWindowTitle();
}
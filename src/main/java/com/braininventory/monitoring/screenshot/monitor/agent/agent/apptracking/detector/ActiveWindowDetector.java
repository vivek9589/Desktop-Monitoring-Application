package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector;



/**
 * Provides active window and application details.
 */
public interface ActiveWindowDetector {

    String getActiveApp();

    String getWindowTitle();
}
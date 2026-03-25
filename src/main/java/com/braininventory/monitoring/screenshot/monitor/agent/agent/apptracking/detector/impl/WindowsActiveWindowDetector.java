package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Windows-specific implementation using JNA.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "os.type", havingValue = "windows")
public class WindowsActiveWindowDetector implements ActiveWindowDetector {

    @Override
    public String getActiveApp() {
        try {
            WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();

            if (hwnd == null) {
                log.warn("No active window found");
                return "unknown";
            }

            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

            int processId = pid.getValue();

            Tlhelp32.PROCESSENTRY32.ByReference processEntry =
                    new Tlhelp32.PROCESSENTRY32.ByReference();

            WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
                    Tlhelp32.TH32CS_SNAPPROCESS,
                    new WinDef.DWORD(0)
            );

            try {
                // FIX: Must call Process32First first
                if (Kernel32.INSTANCE.Process32First(snapshot, processEntry)) {
                    do {
                        if (processEntry.th32ProcessID.intValue() == processId) {
                            String processName = Native.toString(processEntry.szExeFile);
                            log.debug("Active process detected: {}", processName);
                            return processName;
                        }
                    } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                }
            } finally {
                Kernel32.INSTANCE.CloseHandle(snapshot);
            }

            return "unknown";

        } catch (Exception e) {
            log.error("Failed to get active app", e);
            return "unknown";
        }
    }

    @Override
    public String getWindowTitle() {
        try {
            WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();

            if (hwnd == null) {
                log.warn("No active window for title");
                return "unknown";
            }

            char[] windowText = new char[1024];
            User32.INSTANCE.GetWindowText(hwnd, windowText, 1024);

            String title = Native.toString(windowText);

            log.debug("Active window title: {}", title);

            return title;

        } catch (Exception e) {
            log.error("Failed to get window title", e);
            return "unknown";
        }
    }
}
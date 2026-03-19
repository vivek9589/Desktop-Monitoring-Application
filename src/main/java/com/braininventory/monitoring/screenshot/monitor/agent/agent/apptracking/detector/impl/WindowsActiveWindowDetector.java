package com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.impl;

import com.braininventory.monitoring.screenshot.monitor.agent.agent.apptracking.detector.ActiveWindowDetector;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


@Component
@ConditionalOnProperty(name = "os.type", havingValue = "windows")
public class WindowsActiveWindowDetector implements ActiveWindowDetector {

    @Override
    public String getActiveApp() {
        try {
            WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();

            IntByReference pid = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);

            int processId = pid.getValue();

            //  Use Toolhelp to get process name
            Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();

            WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
                    Tlhelp32.TH32CS_SNAPPROCESS,
                    new WinDef.DWORD(0)
            );

            try {
                while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry)) {
                    if (processEntry.th32ProcessID.intValue() == processId) {
                        return Native.toString(processEntry.szExeFile);
                    }
                }
            } finally {
                Kernel32.INSTANCE.CloseHandle(snapshot);
            }

            return "unknown";

        } catch (Exception e) {
            return "unknown";
        }
    }

    @Override
    public String getWindowTitle() {
        try {
            char[] windowText = new char[1024];

            WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
            User32.INSTANCE.GetWindowText(hwnd, windowText, 1024);

            return Native.toString(windowText);

        } catch (Exception e) {
            return "unknown";
        }
    }
}

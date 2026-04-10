package com.braininventory.monitoring.agent.apptracking.detector;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface Psapi extends StdCallLibrary {
    Psapi INSTANCE = Native.load("Psapi", Psapi.class);

    int GetModuleBaseName(
            WinNT.HANDLE hProcess,
            WinDef.HMODULE hModule,
            char[] lpBaseName,
            int nSize
    );
}

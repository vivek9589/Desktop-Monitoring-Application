package com.braininventory.monitoring.screenshot.monitor.agent.backend.controller;


import com.braininventory.monitoring.screenshot.monitor.agent.common.dto.request.ActiveTabDto;
import com.braininventory.monitoring.screenshot.monitor.agent.backend.service.ActiveTabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActiveTabController {

    private final ActiveTabService activeTabService;

    @PostMapping("/active-tab")
    public void receiveActiveTab(@RequestBody ActiveTabDto dto) {
        activeTabService.update(dto);
    }
}

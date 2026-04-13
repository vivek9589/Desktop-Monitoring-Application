package com.braininventory.monitoring.server.module.activitybackend.controller;

import com.braininventory.monitoring.common.dto.request.ActiveTabDto;
import com.braininventory.monitoring.server.module.activitybackend.service.ActiveTabService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/active-tab")
@RequiredArgsConstructor
public class ActiveTabController {
    private final ActiveTabService activeTabService;

    @PostMapping
    public void receiveActiveTab(@RequestBody ActiveTabDto dto) {
        activeTabService.update(dto);
    }

    @GetMapping("/current")
    public ActiveTabDto getCurrentTab() {
        return new ActiveTabDto(activeTabService.getCurrentUrl(), activeTabService.getCurrentTitle());
    }
}

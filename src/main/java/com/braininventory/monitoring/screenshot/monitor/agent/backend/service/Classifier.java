package com.braininventory.monitoring.screenshot.monitor.agent.backend.service;


import com.braininventory.monitoring.screenshot.monitor.agent.backend.enums.Category;

public interface Classifier<T> {
    Category classify(T input);
}
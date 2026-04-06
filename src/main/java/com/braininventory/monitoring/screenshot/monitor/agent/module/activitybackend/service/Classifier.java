package com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.service;


import com.braininventory.monitoring.screenshot.monitor.agent.module.activitybackend.enums.Category;

public interface Classifier<T> {
    Category classify(T input);
}
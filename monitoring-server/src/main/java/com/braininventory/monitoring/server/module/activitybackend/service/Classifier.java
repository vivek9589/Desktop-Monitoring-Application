package com.braininventory.monitoring.server.module.activitybackend.service;


import com.braininventory.monitoring.server.module.activitybackend.enums.Category;

public interface Classifier<T> {
    Category classify(T input);
}
package com.braininventory.monitoring.server.module.activitybackend.entity;


import com.braininventory.monitoring.server.module.activitybackend.enums.Category;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_classification")
@Data
public class AppClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String appName;

    @Enumerated(EnumType.STRING)
    private Category category;
}
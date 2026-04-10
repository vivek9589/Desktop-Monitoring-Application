package com.braininventory.monitoring.server.module.device.entity;


import com.braininventory.monitoring.server.module.device.DeviceCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "device_identifier", unique = true, nullable = false, length = 100)
    private String deviceIdentifier;

    @Column(name = "machine_name", nullable = false, length = 150)
    private String machineName;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private DeviceCategory category =  DeviceCategory.PERSONAL;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
}


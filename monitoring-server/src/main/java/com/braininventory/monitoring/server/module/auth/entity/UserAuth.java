package com.braininventory.monitoring.server.module.auth.entity;



import com.braininventory.monitoring.server.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuth {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String passwordHash;


}
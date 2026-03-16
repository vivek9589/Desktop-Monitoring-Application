package com.braininventory.monitoring.screenshot.monitor.agent.auth.repository;


import com.braininventory.monitoring.screenshot.monitor.agent.auth.entity.UserAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
    Optional<UserAuth> findByEmail(String email);
}
package com.braininventory.monitoring.server.module.user.service.impl;

import com.braininventory.monitoring.common.exception.OrganizationNotFoundException;
import com.braininventory.monitoring.server.module.notification.service.NotificationService;
import com.braininventory.monitoring.server.module.organization.entity.Organization;
import com.braininventory.monitoring.server.module.organization.repository.OrganizationRepository;
import com.braininventory.monitoring.server.module.user.entity.Invitation;
import com.braininventory.monitoring.server.module.user.repository.InvitationRepository;
import com.braininventory.monitoring.server.module.user.repository.UserRepository;
import com.braininventory.monitoring.server.module.user.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public void sendBulkInvites(List<String> emails, UUID orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found"));

        for (String email : emails) {
            // 1. Skip if already a registered user
            if (userRepository.findByEmail(email).isPresent()) {
                log.warn("Skipping invitation: User {} already exists", email);
                continue;
            }

            // 2. Generate unique token and save invitation
            String token = UUID.randomUUID().toString();
            Invitation invitation = Invitation.builder()
                    .email(email)
                    .token(token)
                    .organization(org)
                    .expiryDate(LocalDateTime.now().plusDays(7)) // 7 days validity
                    .build();

            invitationRepository.save(invitation);

            // 3. Send the email
            notificationService.sendInvitationEmail(email, token, org.getName());
        }
    }
}

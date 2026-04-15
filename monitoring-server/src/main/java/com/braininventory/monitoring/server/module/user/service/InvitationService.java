package com.braininventory.monitoring.server.module.user.service;

import java.util.List;
import java.util.UUID;

public interface InvitationService {

    void sendBulkInvites(List<String> emails, UUID orgId);
}

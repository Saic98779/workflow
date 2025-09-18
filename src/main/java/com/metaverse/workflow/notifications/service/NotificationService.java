package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.model.Notifications;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    Notifications sendFromCallCenterToAgency(com.metaverse.workflow.notifications.dto.NotificationRequestDto dto);
    Notifications sendFromAgencyToCallCenter(com.metaverse.workflow.notifications.dto.NotificationRequestDto dto);
    List<Notifications> getAllByAgency(Long agencyId);
    List<Notifications> getAllByCallCenterUser(Long userId);
}

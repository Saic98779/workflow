package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.GlobalNotifications;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.notifications.dto.GlobalNotificationDTO;
import com.metaverse.workflow.notifications.dto.GlobalNotificationMapper;
import com.metaverse.workflow.notifications.repository.GlobalNotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GlobalNotificationServiceImpl {

    private final GlobalNotificationRepository notificationRepository;
    private final LoginRepository userRepository;
    private final AgencyRepository agencyRepository;

    @Transactional
    public WorkflowResponse sendNotification(GlobalNotificationDTO dto) {
        User createdBy = userRepository.findById(dto.getCreatedById()).orElseThrow(() -> new RuntimeException("Sender user not found"));
        User recipient = userRepository.findById(dto.getRecipientUserId()).orElseThrow(() -> new RuntimeException("Recipient user not found"));
        Agency agency = dto.getAgencyId() != null ? agencyRepository.findById(dto.getAgencyId()).orElseThrow(() -> new RuntimeException("Agency not found")):null;

        GlobalNotifications globalNotifications =GlobalNotifications.builder()
                .message(dto.getMessage())
                .remarkBy(dto.getRemarkBy())
                .recipientType(dto.getRecipientType())
                .recipientUser(recipient)
                .createdBy(createdBy)
                .agency(agency)
                .dateOfNotification(DateUtil.covertStringToDate(dto.getDateOfNotification()))
                .isRead(false)
                .build();
        return WorkflowResponse.builder()
                .data(GlobalNotificationMapper.toDto(notificationRepository.save(globalNotifications)))
                .message("Success")
                .status(200).build();
    }

    public WorkflowResponse getNotificationsForUser(String userId) {
        List<GlobalNotificationDTO> globalNotificationDTOList= notificationRepository.findByRecipientUser_UserId(userId)
                .stream().map(GlobalNotificationMapper::toDto).collect(Collectors.toList());
        return WorkflowResponse.builder()
                .data(globalNotificationDTOList)
                .message("Success")
                .status(200).build();
    }

    public WorkflowResponse getNotificationsForRecipientType(NotificationRecipientType type, Long agencyId) {
        List<GlobalNotificationDTO> globalNotificationDTOList;

        if (agencyId != null) {
            //  Filter by both recipient type and agency
            globalNotificationDTOList = notificationRepository
                    .findByRecipientTypeAndAgency_AgencyId(type, agencyId)
                    .stream()
                    .map(GlobalNotificationMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            //  Fetch all notifications for that recipient type
            globalNotificationDTOList = notificationRepository
                    .findByRecipientType(type)
                    .stream()
                    .map(GlobalNotificationMapper::toDto)
                    .collect(Collectors.toList());
        }

        return WorkflowResponse.builder()
                .data(globalNotificationDTOList)
                .message("Success")
                .status(200)
                .build();
    }

    @Transactional
    public WorkflowResponse markAsRead(Long notificationId) {
        GlobalNotifications notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return WorkflowResponse.builder()
                .message("Notification is seen")
                .status(200).build();
    }
}

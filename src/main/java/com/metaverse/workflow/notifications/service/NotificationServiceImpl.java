package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.NotificationRemark;
import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.notifications.dto.NotificationRequestDto;
import com.metaverse.workflow.notifications.dto.NotificationStatusUpdateDto;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final LoginRepository userRepository;
    private final AgencyRepository agencyRepository;

    // 1. Call Center -> Agency
    public Notifications sendFromCallCenterToAgency(NotificationRequestDto dto) {
        User callCenter = userRepository.findById(String.valueOf(dto.getCallCenterUserId()))
                .orElseThrow(() -> new RuntimeException("Call center user not found"));
        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        Notifications notification = Notifications.builder()
                .dateOfNotification(LocalDate.now().atStartOfDay())
                .callCenterAgent(callCenter)
                .agency(agency)
                .status(NotificationStatus.OPEN)
                .recipientType(NotificationRecipientType.AGENCY)
                .build();

        // Add initial remark from Call Center
        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            NotificationRemark remark = NotificationRemark.builder()
                    .notification(notification)
                    .remarkBy(RemarkBy.CALL_CENTER)
                    .remarkText(dto.getMessage())
                    .remarkedAt(java.time.LocalDateTime.now())
                    .build();
            notification.getRemarksByCallCenter().add(remark);
        }

        return notificationRepository.save(notification);
    }

    // 2. Agency -> Call Center
    public Notifications sendFromAgencyToCallCenter(NotificationRequestDto dto) {
        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        User callCenter = userRepository.findById(String.valueOf(dto.getCallCenterUserId()))
                .orElseThrow(() -> new RuntimeException("Call center user not found"));

        Notifications notification = Notifications.builder()
                .dateOfNotification(LocalDate.now().atStartOfDay())
                .agency(agency)
                .callCenterAgent(callCenter)
                .status(NotificationStatus.OPEN)
                .recipientType(NotificationRecipientType.CALL_CENTER)
                .build();

        // Add initial remark from Agency
        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            NotificationRemark remark = NotificationRemark.builder()
                    .notification(notification)
                    .remarkBy(RemarkBy.AGENCY)
                    .remarkText(dto.getMessage())
                    .remarkedAt(java.time.LocalDateTime.now())
                    .build();
            notification.getRemarksByAgency().add(remark);
        }
        return notificationRepository.save(notification);
    }

    // 3. Get all notifications by Agency
    public List<Notifications> getAllByAgencyAndStatuses(Long agencyId, List<NotificationStatus> statuses) {
        return notificationRepository.findByAgency_AgencyIdAndStatusIn(agencyId, statuses);
    }

    public List<Notifications> getAllByAgency(Long agencyId) {
        return notificationRepository.findByAgency_Id(agencyId);
    }

    // 4. Get all notifications by Call Center Agent (userId)
    public List<Notifications> getAllByCallCenterUser(String userId) {
        return notificationRepository.findByCallCenterAgent_UserId(userId);
    }


    public List<Notifications> getAllByCallCenterUserAndStatuses(String userId, List<NotificationStatus> statuses) {
        return notificationRepository.findByCallCenterAgent_UserIdAndStatusIn(userId, statuses);
    }

    public Notifications updateStatus(NotificationStatusUpdateDto dto) {
        Notifications notification = notificationRepository.findById(dto.getNotificationId())
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + dto.getNotificationId()));

        notification.setStatus(dto.getStatus());

        if (dto.getRemark() != null && !dto.getRemark().isBlank()) {
            NotificationRemark remark = NotificationRemark.builder()
                    .notification(notification)
                    .remarkBy(dto.getRemarkBy())
                    .remarkText(dto.getRemark())
                    .remarkedAt(java.time.LocalDateTime.now())
                    .build();

            if (dto.getRemarkBy() == RemarkBy.AGENCY) {
                notification.getRemarksByAgency().add(remark);
            } else {
                notification.getRemarksByCallCenter().add(remark);
            }
        }

        switch (dto.getStatus()) {
            case CLOSED -> notification.setDateOfClosure(java.time.LocalDateTime.now());
            case COMPLETED -> notification.setDateOfFix(java.time.LocalDateTime.now());
            default -> {}
        }

        return notificationRepository.save(notification);
    }
}

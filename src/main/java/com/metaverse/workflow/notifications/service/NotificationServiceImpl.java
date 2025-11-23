package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;

import com.metaverse.workflow.notifications.dto.GlobalNotificationResponse;
import com.metaverse.workflow.notifications.dto.NotificationDto;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final LoginRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramRepository programRepository;

    // =============================================================
    // GLOBAL, UNIVERSAL, ASYNC NOTIFICATION CREATOR
    // =============================================================
    @Async
    public void saveNotification(GlobalNotificationRequest req) {

        // ----------------------------------------------------------
        // 1. CREATE BASE NOTIFICATION
        // ----------------------------------------------------------
        Notifications notification = new Notifications();
        notification.setDateOfNotification(LocalDateTime.now());
        notification.setDateOfFirstNotification(LocalDateTime.now());
        notification.setStatus(NotificationStatus.OPEN);
        notification.setNotificationType(req.getNotificationType());
        notification.setMessages(new ArrayList<>());

        // ----------------------------------------------------------
        // 2. SET RECEIVER USER (GENERIC)
        // ----------------------------------------------------------
        if (req.getUserId() != null && !req.getUserId().equals("-1")) {
            User receiver = userRepository.findByUserId(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + req.getUserId()));
            notification.setReceiver(receiver);
        }

        // ----------------------------------------------------------
        // 3. OPTIONAL AGENCY
        // ----------------------------------------------------------
        if (req.getAgencyId() != null && req.getAgencyId() != -1) {
            Agency agency = agencyRepository.findById(req.getAgencyId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            notification.setAgency(agency);
        }

        // ----------------------------------------------------------
        // 4. OPTIONAL PARTICIPANT
        // ----------------------------------------------------------
        if (req.getParticipantId() != null && req.getParticipantId() != -1) {
            Participant participant = participantRepository.findById(req.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Participant not found"));
            notification.setParticipant(participant);
        }

        // ----------------------------------------------------------
        // 5. OPTIONAL PROGRAM
        // ----------------------------------------------------------
        if (req.getProgramId() != null && req.getProgramId() != -1) {
            Program program = programRepository.findByProgramId(req.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));
            notification.setProgram(program);
        }

        // ----------------------------------------------------------
        // 6. DECIDE RECIPIENT TYPE BASED ON SENDER
        // ----------------------------------------------------------
        if (req.getSentBy() == RemarkBy.AGENCY) {
            if(req.getParticipantId() != -1){
                notification.setRecipientType(NotificationRecipientType.CALL_CENTER);
            }
            notification.setRecipientType(NotificationRecipientType.ADMIN);

        } else if (req.getSentBy() == RemarkBy.CALL_CENTER) {
            notification.setRecipientType(NotificationRecipientType.AGENCY);

        } else {
            notification.setRecipientType(NotificationRecipientType.AGENCY);
        }

        notification.updateLastMessageTime();
        notification.setIsRead(false);

        // Save base notification first
        Notifications saved = notificationRepository.save(notification);

        // ----------------------------------------------------------
        // 7. ADD FIRST MESSAGE (IF PRESENT)
        // ----------------------------------------------------------
        if (req.getMessage() != null && !req.getMessage().isBlank()) {

            NotificationMessage msg = NotificationMessage.builder()
                    .notification(saved)
                    .text(req.getMessage())
                    .sentBy(req.getSentBy())
                    .createdAt(LocalDateTime.now())
                    .build();

            saved.getMessages().add(msg);
            saved.updateLastMessageTime();

            notificationRepository.save(saved);
        }
    }

    public NotificationDto getNotificationsByRole(String role) {

        List<Notifications> list = notificationRepository.findByReceiverRole(role);

        if (list.isEmpty()) return null;

        // Base notification = the most recent one OR first one
        Notifications base = list.stream()
                .max(Comparator.comparing(Notifications::getLastMessageAt))
                .orElse(list.get(0));

        // Merge all messages from all notifications
        List<NotificationMessage> allMessages = list.stream()
                .flatMap(n -> n.getMessages().stream())
                .sorted(Comparator.comparing(NotificationMessage::getCreatedAt).reversed())
                .toList();

        // Build DTO manually
        return NotificationDto.builder()
                .notificationId(base.getId())
                .receiverId(base.getReceiver().getUserId())
                .receiverName(base.getReceiver().getFirstName() + " " + base.getReceiver().getLastName())
                .receiverRole(base.getReceiver().getUserRole())
                .status(base.getStatus())
                .isRead(base.getIsRead())
                .recipientType(base.getRecipientType())
                .lastMessageAt(base.getLastMessageAt())
                .messages(allMessages.stream()
                        .map(msg -> NotificationMapper.toMessageDto(msg, base))
                        .toList()
                )
                .build();
    }


    public List<NotificationDto> getNotificationsForUser(String userId) {
        return notificationRepository.findByReceiver_UserIdOrderByLastMessageAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    public NotificationDto getNotificationById(Long id) {
        Notifications n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return NotificationMapper.toDto(n);
    }

    public void markAsFixed(Long id) {
        Notifications n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setDateOfFix(LocalDateTime.now());
        n.setStatus(NotificationStatus.COMPLETED);
        notificationRepository.save(n);
    }

    public void markAsClosed(Long id) {
        Notifications n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setDateOfClosure(LocalDateTime.now());
        n.setStatus(NotificationStatus.CLOSED);
        notificationRepository.save(n);
    }

    public List<GlobalNotificationResponse> getAllUnReadNotifications(Long agencyId, Boolean isRead) {

        List<Notifications> unReadNotifications = notificationRepository.findByIsReadAndAgency_agencyId(isRead, agencyId);

        return unReadNotifications.stream().filter(notification -> notification.getMessages().get(0).getSentBy() == RemarkBy.ADMIN).map(notification ->
                GlobalNotificationResponse.builder()
                        .notificationId(notification.getId())
                        .agencyId(notification.getAgency().getAgencyId())
                        .isRead(notification.getIsRead())
                        .sentBy(notification.getMessages().get(0).getSentBy())
                        .notificationType(notification.getNotificationType())
                        .createdAt(notification.getMessages().get(0).getCreatedAt())
                        .message(notification.getMessages().get(0).getText())
                        .build()).toList();
    }

    public Notifications updateIsRead(Long notificationId, Boolean isRead) {

        Notifications notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(isRead);

        return notificationRepository.save(notification);
    }

}

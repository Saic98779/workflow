package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.enums.RemarkBy;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.notifications.dto.NotificationRequestDto;
import com.metaverse.workflow.notifications.dto.NotificationStatusUpdateDto;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {

    private final NotificationRepository notificationRepository;
    private final LoginRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramRepository programRepository;

    // 1. Call Center -> Agency
    public Notifications sendFromCallCenterToAgency(NotificationRequestDto dto) {

        Optional<Object> callCenter = Optional.empty();
        if(!dto.getCallCenterUserId().equals("-1")) {
             callCenter = userRepository.findByUserId(dto.getCallCenterUserId());
        }
        Agency agency = null;
        if(!dto.getAgencyId().equals("-1")) {
            agency  = agencyRepository.findById(dto.getAgencyId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
        }
        Participant participant = null;
        if (dto.getParticipantId() != null && !dto.getParticipantId().equals(-1L)) {
            participant = participantRepository.findById(dto.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Participant not found"));
        }

        Program program = null;
        if (dto.getProgramId() != null && !dto.getProgramId().equals(-1L)) {
            program = (Program) programRepository.findByProgramId(dto.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));
        }

        Notifications notification = Notifications.builder()
                .dateOfNotification(LocalDate.now().atStartOfDay())
                .dateOfFirstNotification(LocalDate.now().atStartOfDay())
                .callCenterAgent(callCenter.isPresent() ? (callCenter.get() instanceof User ? (User) callCenter.get() : null) : null)
                .agency(agency)
                .status(NotificationStatus.OPEN)
                .participant(participant)
                .program(program)
                .remarksByAgency(new ArrayList<>())
                .remarksByCallCenter(new ArrayList<>())
                .recipientType(NotificationRecipientType.AGENCY)
                .build();

        // Add initial remark from Call Center
        if (dto.getMessage() != null && !dto.getMessage().isBlank()) {
            NotificationRemark remark = NotificationRemark.builder()
                    .notification(notification)
                    .remarkBy(dto.getProgramId().equals(-1L) ? RemarkBy.ADMIN :  RemarkBy.CALL_CENTER)
                    .remarkText(dto.getMessage())
                    .remarkedAt(java.time.LocalDateTime.now())
                    .build();
            notification.setRemarksByCallCenter(List.of(remark));
            notification.setRemarksByAgency(List.of());
        }

        return notificationRepository.save(notification);
    }

    // 2. Agency -> Call Center
    public Notifications sendFromAgencyToCallCenter(NotificationRequestDto dto) {

        Agency agency = null;
        if (dto.getAgencyId() != null && !dto.getAgencyId().equals(-1L))
            agency =  agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        User callCenter = null;
        if (dto.getCallCenterUserId() != null && !dto.getCallCenterUserId().equals("-1"))
                userRepository.findById(dto.getCallCenterUserId())
                .orElseThrow(() -> new RuntimeException("Call center user not found"));

        Participant participant = null;
        if (dto.getParticipantId() != null && !dto.getParticipantId().equals(-1L)) {
            participant = participantRepository.findById(dto.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Participant not found"));
        }

        Program program = null;
        if (dto.getProgramId() != null && !dto.getProgramId().equals(-1L)) {
            program = (Program) programRepository.findByProgramId(dto.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found"));
        }

        Notifications notification = Notifications.builder()
                .dateOfNotification(LocalDate.now().atStartOfDay())
                .dateOfFirstNotification(LocalDate.now().atStartOfDay())
                .agency(agency)
                .callCenterAgent(callCenter)
                .participant(participant)
                .program(program)
                .status(NotificationStatus.OPEN)
                .recipientType(dto.getProgramId().equals(-1L) ? NotificationRecipientType.ADMIN :NotificationRecipientType.CALL_CENTER)
                .remarksByAgency(new ArrayList<>())
                .remarksByCallCenter(new ArrayList<>())
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
        return notificationRepository.findByAgency_AgencyIdAndStatusInOrderByRemarkedAtDesc(agencyId, statuses);
    }

    public List<Notifications> getAllByAgency(Long agencyId) {
        return notificationRepository.findByAgency_AgencyIdAndStatusInOrderByRemarkedAtDesc(agencyId,
                List.of(NotificationStatus.OPEN, NotificationStatus.IN_PROGRESS));
    }

    // 4. Get all notifications by Call Center Agent (userId)
    public List<Notifications> getAllByCallCenterUser(String userId) {
        return notificationRepository.findByCallCenterAgent_UserIdAndStatusInOrderByRemarkedAtDesc(userId,
                List.of(NotificationStatus.OPEN, NotificationStatus.IN_PROGRESS));
    }


    public List<Notifications> getAllByCallCenterUserAndStatuses(String userId, List<NotificationStatus> statuses) {
        return notificationRepository.findByCallCenterAgent_UserIdAndStatusInOrderByRemarkedAtDesc(userId, statuses);
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
            case OPEN -> notification.setDateOfFirstNotification(java.time.LocalDateTime.now());
            default -> {}
        }

        return notificationRepository.save(notification);
    }
}

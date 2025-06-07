package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.model.Notification;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationResponse;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final ProgramRepository programRepository;

    @Override
    @Async
    public CompletableFuture<NotificationResponse> saveNotification(NotificationRequest request) {

        Program program = null;

        if (request.getProgramId() != null) {
            program = programRepository.findById(request.getProgramId()).get();
        }

        Notification notification = Notification.builder()
                .program(program)
                .callCenterId(request.getCallCenterId())
                .userType(request.getUserType().toString())
                .message(request.getMessage())
                .build();

        Notification saved = notificationRepository.save(notification);

        NotificationResponse response = new NotificationResponse();
        response.setId(saved.getId());
        response.setProgramId(request.getProgramId());
        response.setCallCenterId(saved.getCallCenterId());
        response.setMessage(saved.getMessage());

        return CompletableFuture.completedFuture(response);
    }

}

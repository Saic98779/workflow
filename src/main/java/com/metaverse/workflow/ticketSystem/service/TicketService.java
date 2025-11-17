package com.metaverse.workflow.ticketSystem.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.TicketStatus;
import com.metaverse.workflow.enums.RemarkBy;

import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import com.metaverse.workflow.program.service.ProgramServiceAdapter;
import com.metaverse.workflow.ticketSystem.dto.TicketCommentDto;
import com.metaverse.workflow.ticketSystem.dto.TicketDto;
import com.metaverse.workflow.ticketSystem.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TicketHistoryRepository historyRepo;
    private final ProgramServiceAdapter programServiceAdapter;
    private final LoginRepository userRepository;
    private final NotificationServiceImpl notificationService;
    private final RoleTicketStatusRepository roleTicketStatusRepository;

    // ========================= CREATE TICKET =========================

    public WorkflowResponse createTicket(TicketDto dto, List<MultipartFile> files) {
        try {

            Ticket ticket = new Ticket();
            ticket.setTitle(dto.getTitle());
            ticket.setTicketId(UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            ticket.setDescription(dto.getDescription());
            ticket.setPriority(dto.getPriority());
            ticket.setType(dto.getType());
            ticket.setStatus(TicketStatus.CREATED);

            if (dto.getAssigneeId() != null) {
                ticket.setAssignee(getUser(dto.getAssigneeId()));
            }
            if (dto.getReporterId() != null) {
                ticket.setReporter(getUser(dto.getReporterId()));
            }

            ticket = ticketRepo.save(ticket);

            // Attachments
            if (files != null && !files.isEmpty()) {
                List<TicketAttachment> attachments = createAttachmentsFromFiles(files, ticket);
                ticket.getAttachments().addAll(attachments);
                ticketRepo.save(ticket);
            }

            // 🔔 Send notification to ASSIGNEE (Unified Notification)
            if (ticket.getAssignee() != null) {

                GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                        .userId(ticket.getAssignee().getUserId())        // receiver
                        .message("A new ticket is assigned to you: " + ticket.getTicketId())
                        .sentBy(RemarkBy.ADMIN)                           // system action
                        .agencyId(-1L)
                        .participantId(-1L)
                        .programId(-1L)
                        .build();

                notificationService.saveNotification(req);
            }

            return WorkflowResponse.success("Ticket created successfully", new TicketDto(ticket));

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to create ticket: " + e.getMessage());
        }
    }

    private List<TicketAttachment> createAttachmentsFromFiles(List<MultipartFile> files, Ticket ticket) {
        List<String> filePaths = programServiceAdapter
                .storageProgramFiles(files, ticket.getId(), "ticket_attachments");

        List<TicketAttachment> attachments = new ArrayList<>(files.size());

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String path = filePaths.get(i);

            TicketAttachment attachment = new TicketAttachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachment.setFilePath(path);
            attachment.setTicket(ticket);

            attachments.add(attachment);
        }
        return attachments;
    }

    // ========================= UPDATE TICKET =========================

    @Transactional
    public WorkflowResponse updateTicket(String ticketId, TicketDto dto) {
        try {
            Ticket ticket = ticketRepo.findByTicketId(ticketId);
            if (ticket == null) return WorkflowResponse.error("Ticket not found");

            User fromUser = dto.getComments().get(0).getAuthorId() != null ? getUser(dto.getComments().get(0).getAuthorId()) : null;
            User toUser = dto.getAssigneeId() != null ? getUser(dto.getAssigneeId()) : null;

            TicketStatus oldStatus = ticket.getStatus();
            User actor = null;

            // ================= ASSIGNEE CHANGE =================
            if (dto.getAssigneeId() != null) {
                actor = getUser(dto.getAssigneeId());
                ticket.setAssignee(actor);

                GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                        .userId(actor.getUserId())
                        .message("You have been assigned ticket: " + ticket.getTicketId())
                        .sentBy(RemarkBy.ADMIN)
                        .agencyId(-1L)
                        .programId(-1L)
                        .participantId(-1L)
                        .build();

                notificationService.saveNotification(req);
            }

            // Basic field updates
            Optional.ofNullable(dto.getTitle()).ifPresent(ticket::setTitle);
            Optional.ofNullable(dto.getDescription()).ifPresent(ticket::setDescription);
            Optional.ofNullable(dto.getPriority()).ifPresent(ticket::setPriority);
            Optional.ofNullable(dto.getType()).ifPresent(ticket::setType);

            // ================= STATUS UPDATE =================
            if (dto.getStatus() != null) {

                TicketStatus newStatus = dto.getStatus();
                ticket.setStatus(newStatus);

                if (newStatus == TicketStatus.CLOSED) {
                    ticket.setClosedDate(new Date());
                }

                saveHistory(ticket, oldStatus, newStatus, actor, fromUser, toUser,
                        "Status changed to " + newStatus);

                // Notify assignee about status change
                if (ticket.getAssignee() != null) {

                    GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                            .userId(ticket.getAssignee().getUserId())
                            .message("Ticket " + ticket.getTicketId() + " status updated to: " + newStatus)
                            .sentBy(RemarkBy.ADMIN)
                            .agencyId(-1L)
                            .programId(-1L)
                            .participantId(-1L)
                            .build();

                    notificationService.saveNotification(req);
                }
            }

            // ================= COMMENTS ADDED =================
            if (dto.getComments() != null && !dto.getComments().isEmpty()) {

                for (TicketCommentDto commentDto : dto.getComments()) {

                    if (commentDto.getMessage() == null || commentDto.getMessage().isBlank())
                        continue;

                    User commenter = commentDto.getAuthorId() != null
                            ? getUser(commentDto.getAuthorId())
                            : ticket.getReporter();

                    TicketComment comment = new TicketComment();
                    comment.setMessage(commentDto.getMessage());
                    comment.setTicket(ticket);
                    comment.setAuthor(commenter);

                    ticket.getComments().add(comment);

                    // Notify assignee about comment
                    if (ticket.getAssignee() != null) {

                        GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                                .userId(ticket.getAssignee().getUserId())
                                .message("New comment added on ticket: " + ticket.getTicketId())
                                .sentBy(RemarkBy.CALL_CENTER)
                                .agencyId(-1L)
                                .programId(-1L)
                                .participantId(-1L)
                                .build();

                        notificationService.saveNotification(req);
                    }
                }
            }

            Ticket updatedTicket = ticketRepo.save(ticket);

            return WorkflowResponse.success("Ticket updated successfully", new TicketDto(updatedTicket));

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to update ticket: " + e.getMessage());
        }
    }

    private User getUser(String userId) {
        if (userId == null) return null;
        return userRepository.findByUserId(userId).orElse(null);
    }

    private void saveHistory(Ticket ticket, TicketStatus from, TicketStatus to,
                             User actor, User fromUser, User toUser, String remarks) {

        TicketHistory history = new TicketHistory();
        history.setTicket(ticket);
        history.setUser(actor);
        history.setFromUser(fromUser);
        history.setToUser(toUser);
        history.setFromStatus(from);
        history.setToStatus(to);
        history.setAction("UPDATE");
        history.setRemarks(remarks);

        historyRepo.save(history);
    }

    // ========================= FETCHING & FILTERS =========================

    public WorkflowResponse getAllTickets(List<TicketStatus> statusFilter, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

            List<TicketStatus> finalStatuses = buildStatusList(statusFilter);

            Page<Ticket> ticketPage = ticketRepo.findByStatusIn(finalStatuses, pageable);

            List<TicketDto> ticketDtos = ticketPage.stream().map(TicketDto::new).toList();

            return WorkflowResponse.success("Tickets fetched successfully", ticketDtos,
                    ticketPage.getTotalPages(), ticketPage.getTotalElements());

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch tickets: " + e.getMessage());
        }
    }

    public WorkflowResponse getTicketsForUser(String userId, List<TicketStatus> statusFilter, Pageable pageable) {
        try {
            List<TicketStatus> finalStatuses = buildStatusList(statusFilter);

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("updatedAt").descending());

            Page<Ticket> ticketPage =
                    ticketRepo.findByAssignee_UserIdOrReporter_UserIdAndStatusIn(
                            userId, userId, finalStatuses, pageable
                    );

            return WorkflowResponse.success("Tickets fetched successfully",
                    ticketPage.stream().map(TicketDto::new).toList(),
                    ticketPage.getTotalPages(),
                    ticketPage.getTotalElements());

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch user tickets: " + e.getMessage());
        }
    }

    private List<TicketStatus> buildStatusList(List<TicketStatus> statusFilter) {

        if (statusFilter.size() == 1 && statusFilter.get(0) == TicketStatus.OPEN) {
            return Arrays.stream(TicketStatus.values())
                    .filter(s -> s != TicketStatus.CLOSED)
                    .toList();
        }

        if (statusFilter.size() == 1 && statusFilter.get(0) == TicketStatus.CLOSED) {
            return List.of(TicketStatus.CLOSED);
        }

        return statusFilter;
    }

    public WorkflowResponse getReportById(int page, int size, String reporterId) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            Page<Ticket> ticketPage =
                    ticketRepo.findByReporter_UserIdOrAssignee_UserId(
                            reporterId, reporterId, pageable
                    );

            return WorkflowResponse.success("Tickets fetched successfully",
                    ticketPage.stream().map(TicketDto::new).toList(),
                    ticketPage.getTotalPages(), ticketPage.getTotalElements());

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch report: " + e.getMessage());
        }
    }

    public List<TicketStatus> getStatusesForRole(String role) {
        return roleTicketStatusRepository.findByRoleName(role)
                .stream()
                .map(RoleTicketStatus::getStatusName)
                .toList();
    }

    public WorkflowResponse getTicketById(Long ticketId) {
        try {
            Ticket ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            return WorkflowResponse.success("Ticket details fetched successfully", new TicketDto(ticket));

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch ticket: " + e.getMessage());
        }
    }

}

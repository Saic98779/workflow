package com.metaverse.workflow.ticketSystem.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.TicketStatus;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.program.service.ProgramServiceAdapter;
import com.metaverse.workflow.ticketSystem.dto.TicketCommentDto;
import com.metaverse.workflow.ticketSystem.dto.TicketDto;
import com.metaverse.workflow.ticketSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TicketAttachmentRepository attachmentRepo;
    private final TicketHistoryRepository historyRepo;
    private final ProgramServiceAdapter programServiceAdapter;
    private final LoginRepository userRepository;

    private final Path root = Paths.get("uploads");

    // ----------------------------------------------------------------------
    // 📌 CREATE TICKET
    // ----------------------------------------------------------------------
    public WorkflowResponse createTicket(TicketDto dto, List<MultipartFile> files) {
        try {
            Ticket ticket = new Ticket();
            ticket.setTitle(dto.getTitle());
            //  Generate random token ID
            ticket.setTicketId(UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            ticket.setDescription(dto.getDescription());
            ticket.setPriority(dto.getPriority());
            ticket.setType(dto.getType());
            ticket.setStatus(TicketStatus.CREATED);
            if (dto.getAssigneeId() != null) {
                ticket.setAssignee(getUser(dto.getAssigneeId()));
            }

            // Reporter
            if (dto.getReporterId() != null) {
                User reporter = getUser(dto.getReporterId());
                ticket.setReporter(reporter);
            }

            // Save to generate ID
            ticket = ticketRepo.save(ticket);

            // Save history for creation
            saveHistory(ticket, null, TicketStatus.CREATED, ticket.getReporter(), "Ticket created");

            // 🗂️ Save attachments (if any)
            if (files != null && !files.isEmpty()) {
                List<TicketAttachment> attachments = createAttachmentsFromFiles(files, ticket);
                ticket.getAttachments().addAll(attachments);
            }

            // 💬 Save initial comments (supports multiple)
            if (dto.getComments() != null && !dto.getComments().isEmpty()) {
                for (TicketCommentDto commentDto : dto.getComments()) {
                    if (commentDto.getMessage() == null || commentDto.getMessage().isBlank()) continue;

                    TicketComment comment = new TicketComment();
                    comment.setMessage(commentDto.getMessage());
                    comment.setTicket(ticket);

                    User author = commentDto.getAuthorId() != null
                            ? getUser(commentDto.getAuthorId())
                            : ticket.getReporter();

                    comment.setAuthor(author);
                    ticket.getComments().add(comment);

                    saveHistory(ticket, TicketStatus.CREATED, TicketStatus.CREATED, author,
                            "Initial comment: " + comment.getMessage());
                }
            }

            ticket = ticketRepo.save(ticket);

            return WorkflowResponse.success("Ticket created successfully", new TicketDto(ticket));

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to create ticket: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // 🗂️ Helper: Create attachments from uploaded files
    // ----------------------------------------------------------------------
    private List<TicketAttachment> createAttachmentsFromFiles(List<MultipartFile> files, Ticket ticket) {
        List<String> filePaths = programServiceAdapter.storageProgramFiles(files, ticket.getId(), "ticket_attachments");

        List<TicketAttachment> attachments = new ArrayList<>();
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

    // ----------------------------------------------------------------------
    // 📌 UPDATE TICKET (Supports multiple comments + attachments)
    // ----------------------------------------------------------------------
    @Transactional
    public WorkflowResponse updateTicket(String ticketId, TicketDto dto) {
        try {
            Ticket ticket = ticketRepo.findByTicketId(ticketId);

            TicketStatus oldStatus = ticket.getStatus();
            User actor = null;

            if (dto.getAssigneeId() != null) {
                actor = getUser(dto.getAssigneeId());
                ticket.setAssignee(actor);
            } else if (dto.getReporterId() != null) {
                actor = getUser(dto.getReporterId());
            }

            // Editable fields
            if (dto.getTitle() != null) ticket.setTitle(dto.getTitle());
            if (dto.getDescription() != null) ticket.setDescription(dto.getDescription());
            if (dto.getPriority() != null) ticket.setPriority(dto.getPriority());
            if (dto.getType() != null) ticket.setType(dto.getType());

            // Status change
            if (dto.getStatus() != null) {
                TicketStatus newStatus = dto.getStatus();
                ticket.setStatus(newStatus);
                if (newStatus == TicketStatus.CLOSED) ticket.setClosedDate(new Date());

                saveHistory(ticket, oldStatus, newStatus, actor, "Status changed to " + newStatus);
            }

            // 💬 Handle multiple comments
            if (dto.getComments() != null && !dto.getComments().isEmpty()) {
                for (TicketCommentDto commentDto : dto.getComments()) {
                    if (commentDto.getMessage() == null || commentDto.getMessage().isBlank()) continue;

                    TicketComment comment = new TicketComment();
                    comment.setMessage(commentDto.getMessage());
                    comment.setTicket(ticket);

                    User commenter = commentDto.getAuthorId() != null
                            ? getUser(commentDto.getAuthorId())
                            : ticket.getReporter();

                    comment.setAuthor(commenter);
                    ticket.getComments().add(comment);

                    saveHistory(ticket, ticket.getStatus(), ticket.getStatus(), commenter,
                            "Comment added: " + comment.getMessage());
                }
            }

            // 🔹 Save updated ticket
            Ticket updatedTicket = ticketRepo.save(ticket);
            return WorkflowResponse.success("Ticket updated successfully", new TicketDto(updatedTicket));

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to update ticket: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // 🔹 Utility Methods
    // ----------------------------------------------------------------------
    private User getUser(String userId) {
        if (userId == null) return null;
        return userRepository.findByUserId(userId)
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .orElse(null);
    }

    private void saveHistory(Ticket ticket, TicketStatus from, TicketStatus to, User user, String remarks) {
        TicketHistory history = new TicketHistory();
        history.setTicket(ticket);
        history.setUser(user);
        history.setFromStatus(from);
        history.setToStatus(to);
        history.setAction("UPDATE");
        history.setRemarks(remarks);
        historyRepo.save(history);
    }

    // ----------------------------------------------------------------------
    // 📌 FETCH METHODS
    // ----------------------------------------------------------------------
    public WorkflowResponse getAllTickets(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Ticket> ticketPage = ticketRepo.findAll(pageable);

            List<TicketDto> ticketDtos = ticketPage.getContent()
                    .stream()
                    .map(TicketDto::new)
                    .collect(Collectors.toList());

            return WorkflowResponse.success("Tickets fetched successfully",
                    ticketDtos, ticketPage.getTotalPages(), ticketPage.getTotalElements());

        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch tickets: " + e.getMessage());
        }
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

    public WorkflowResponse getReportById(int page, int size, String reporterId) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Ticket> ticket = ticketRepo.findByReporter_UserIdOrAssignee_UserId(reporterId, reporterId, pageable);
            List<TicketDto> ticketDtos = ticket.getContent()
                    .stream()
                    .map(TicketDto::new)
                    .collect(Collectors.toList());
            return WorkflowResponse.success("Tickets fetched successfully",
                    ticketDtos, ticket.getTotalPages(), ticket.getTotalElements());
        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch Report: " + e.getMessage());
        }
    }

}

package com.metaverse.workflow.ticketSystem.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.Ticket;
import com.metaverse.workflow.model.TicketAttachment;
import com.metaverse.workflow.model.TicketComment;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.program.service.ProgramServiceAdapter;
import com.metaverse.workflow.ticketSystem.dto.TicketDto;
import com.metaverse.workflow.ticketSystem.repository.TicketAttachmentRepository;
import com.metaverse.workflow.ticketSystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TicketAttachmentRepository attachmentRepo;
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
            ticket.setDescription(dto.getDescription());
            ticket.setPriority(dto.getPriority());
            ticket.setStatus("OPEN");
            ticket.setType(dto.getType());

            ticket = ticketRepo.save(ticket);

            if (files != null && !files.isEmpty()) {
                List<TicketAttachment> attachments = createAttachmentsFromFiles(files, ticket);
                ticket.getAttachments().addAll(attachments);
                ticketRepo.save(ticket);
            }

            return WorkflowResponse.success("Ticket created successfully", new TicketDto(ticket));
        } catch (Exception e) {
            return WorkflowResponse.error("Failed to create ticket: " + e.getMessage());
        }
    }

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
    // 📌 UPDATE TICKET
    // ----------------------------------------------------------------------
    @Transactional
    public WorkflowResponse updateTicket(Long ticketId, TicketDto dto) {
        try {
            Ticket ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            if (dto.getTitle() != null) ticket.setTitle(dto.getTitle());
            if (dto.getDescription() != null) ticket.setDescription(dto.getDescription());
            if (dto.getPriority() != null) ticket.setPriority(dto.getPriority());
            if (dto.getStatus() != null) ticket.setStatus(dto.getStatus());
            if (dto.getType() != null) ticket.setType(dto.getType());

            if ("CLOSED".equalsIgnoreCase(dto.getStatus())) {
                ticket.setClosedDate(new Date());
            }

            if (dto.getAssigneeId() != null) {
                Optional<Object> assigneeOpt = userRepository.findByUserId(dto.getAssigneeId());
                if (assigneeOpt.isPresent() && assigneeOpt.get() instanceof User) {
                    ticket.setAssignee((User) assigneeOpt.get());
                } else {
                    throw new RuntimeException("Assignee not found for ID: " + dto.getAssigneeId());
                }
            }

            if (dto.getReporterId() != null) {
                Optional<Object> reporterOpt = userRepository.findByUserId(dto.getReporterId());
                if (reporterOpt.isPresent() && reporterOpt.get() instanceof User) {
                    ticket.setReporter((User) reporterOpt.get());
                } else {
                    throw new RuntimeException("Reporter not found for ID: " + dto.getReporterId());
                }
            }

            // Add comment if provided
            if (dto.getComments() != null) {
                TicketComment comment = new TicketComment();
                comment.setMessage(dto.getComments().getMessage());
                comment.setTicket(ticket);

                if (dto.getComments().getAuthorId() != null) {
                    Optional<Object> commenterOpt = userRepository.findByUserId(dto.getComments().getAuthorId());
                    if (commenterOpt.isPresent() && commenterOpt.get() instanceof User) {
                        comment.setAuthor((User) commenterOpt.get());
                    } else {
                        throw new RuntimeException("Commenter not found for ID: " + dto.getComments().getAuthorId());
                    }
                }
                ticket.getComments().add(comment);
            }

            Ticket updatedTicket = ticketRepo.save(ticket);
            return WorkflowResponse.success("Ticket updated successfully", new TicketDto(updatedTicket));
        } catch (Exception e) {
            return WorkflowResponse.error("Failed to update ticket: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // 📌 GET ALL TICKETS (with pagination)
    // ----------------------------------------------------------------------
    public WorkflowResponse getAllTickets(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Ticket> ticketPage = ticketRepo.findAll(pageable);

            List<TicketDto> ticketDtos = ticketPage.getContent()
                    .stream()
                    .map(TicketDto::new)
                    .collect(Collectors.toList());

            return WorkflowResponse.success(
                    "Tickets fetched successfully",
                    ticketDtos,
                    ticketPage.getTotalPages(),
                    ticketPage.getTotalElements()
            );
        } catch (Exception e) {
            return WorkflowResponse.error("Failed to fetch tickets: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // 📌 GET TICKET BY ID
    // ----------------------------------------------------------------------
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

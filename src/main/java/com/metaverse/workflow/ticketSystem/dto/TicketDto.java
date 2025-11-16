package com.metaverse.workflow.ticketSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metaverse.workflow.enums.TicketStatus;
import com.metaverse.workflow.model.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;
    private String ticketId;
    private String title;
    private String description;
    private TicketStatus status;
    private String priority;
    private String type;

    private String assigneeId;
    private String assigneeName;
    private String reporterId;
    private String reporterName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date closedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedAt;

    private List<TicketAttachmentDto> attachments;

    private List<TicketCommentDto> comments;

    private List<TicketHistoryDto> history;

    private String agencyName;


    public TicketDto(Ticket ticket) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.description = ticket.getDescription();
        this.ticketId = ticket.getTicketId();
        this.status = ticket.getStatus();
        this.priority = ticket.getPriority();
        this.type = ticket.getType();
        this.agencyName = ticket.getReporter() != null && ticket.getReporter().getAgency() != null
                ? ticket.getReporter().getAgency().getAgencyName()
                : null;

        if (ticket.getAssignee() != null) {
            this.assigneeId = ticket.getAssignee().getUserId();
            this.assigneeName = ticket.getAssignee().getFirstName() + " " + ticket.getAssignee().getLastName();
        }

        if (ticket.getReporter() != null) {
            this.reporterId = ticket.getReporter().getUserId();
            this.reporterName = ticket.getReporter().getFirstName() + " " + ticket.getReporter().getLastName();
        }

        this.closedDate = ticket.getClosedDate();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();

        this.attachments = ticket.getAttachments() != null
                ? ticket.getAttachments().stream()
                .map(a -> new TicketAttachmentDto(
                        a.getId(),
                        a.getFileName(),
                        a.getFilePath(),
                        a.getContentType()))
                .collect(Collectors.toList())
                : List.of();

        this.comments = ticket.getComments() != null
                ? ticket.getComments().stream()
                .sorted(Comparator.comparing(TicketComment::getCreatedAt).reversed())
                .map(TicketCommentDto::new)
                .collect(Collectors.toList())
                : List.of();


        this.history = ticket.getHistory() != null
                ? ticket.getHistory().stream()
                .sorted(Comparator.comparing(TicketHistory::getCreatedAt).reversed())
                .map(TicketHistoryDto::new)
                .collect(Collectors.toList())
                : List.of();
    }
}

package com.metaverse.workflow.ticketSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metaverse.workflow.model.Ticket;
import com.metaverse.workflow.model.TicketComment;
import lombok.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {

    private Long id;
    private String title;
    private String description;
    private String status;     // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private String priority;   // LOW, MEDIUM, HIGH
    private String type;       // Report issue, Data updation, Feature request

    private String assigneeId;
    private String assigneeName;

    private String reporterId;
    private String reporterName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date closedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedAt;

    private List<TicketAttachmentDto> attachments;
    private TicketCommentDto comments;

    public TicketDto(Ticket ticket) {
        this.id = ticket.getId();
        this.title = ticket.getTitle();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus();
        this.priority = ticket.getPriority();
        this.type = ticket.getType();

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
                .toList()
                : List.of();

        if (ticket.getComments() != null && !ticket.getComments().isEmpty()) {
            TicketComment latestComment = ticket.getComments().stream()
                    .max(Comparator.comparing(TicketComment::getCreatedAt))
                    .orElse(null);
            this.comments = new TicketCommentDto(latestComment);
        }
    }
}

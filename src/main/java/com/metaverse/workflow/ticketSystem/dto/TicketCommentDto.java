package com.metaverse.workflow.ticketSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metaverse.workflow.model.TicketComment;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCommentDto {
    private Long id;
    private String message;
    private String authorId;
    private String authorName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdAt;

    public TicketCommentDto(TicketComment comment) {
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.createdAt = comment.getCreatedAt();

        if (comment.getAuthor() != null) {
            this.authorId = comment.getAuthor().getUserId();
            this.authorName = comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName();
        }
    }
}

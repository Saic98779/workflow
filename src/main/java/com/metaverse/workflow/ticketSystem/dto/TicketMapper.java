package com.metaverse.workflow.ticketSystem.dto;

import com.metaverse.workflow.model.Ticket;

public class TicketMapper {

    public static TicketDto toDto(Ticket ticket) {
        if (ticket == null) return null;

        return TicketDto.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .type(ticket.getType())
                .assigneeId(ticket.getAssignee() != null ? ticket.getAssignee().getUserId() : null)
                .assigneeName(ticket.getAssignee() != null ? ticket.getAssignee().getFirstName() + " " + ticket.getAssignee().getLastName() : null)
                .build();
    }
}


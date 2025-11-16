package com.metaverse.workflow.ticketSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metaverse.workflow.enums.TicketStatus;
import com.metaverse.workflow.model.TicketHistory;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryDto {

    private Long id;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private String action;
    private String remarks;
    private String userId;
    private String userName;
    private String agencyName;
    private String fromUserName;
    private String toUserName;
    private Date createdAt;

    public TicketHistoryDto(TicketHistory history) {
        this.id = history.getId();
        this.fromStatus = history.getFromStatus();
        this.toStatus = history.getToStatus();
        this.action = history.getAction();
        this.remarks = history.getRemarks();

        if (history.getUser() != null) {
            this.userId = history.getUser().getUserId();
            this.userName = history.getUser().getFirstName() + " " + history.getUser().getLastName();
        }

        // Track from user
        if (history.getFromUser() != null) {
            this.fromUserName = history.getFromUser().getFirstName() + " " + history.getFromUser().getLastName();
        }

        // Track to user
        if (history.getToUser() != null) {
            this.toUserName = history.getToUser().getFirstName() + " " + history.getToUser().getLastName();
        }

        if(history.getUser() != null && history.getUser().getAgency() != null)
            this.agencyName= history.getUser().getAgency().getAgencyName();

        this.createdAt = history.getCreatedAt();
    }
}

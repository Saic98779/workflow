package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_history")
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Actor who performed the update
    @ManyToOne
    @JoinColumn(name = "actor_user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private TicketStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private TicketStatus toStatus;

    private String action;

    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // User changed FROM
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    // User changed TO
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}

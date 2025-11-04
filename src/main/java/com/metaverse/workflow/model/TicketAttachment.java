package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ticket_attachments")
public class TicketAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}


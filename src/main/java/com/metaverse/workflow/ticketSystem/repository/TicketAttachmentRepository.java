package com.metaverse.workflow.ticketSystem.repository;

import com.metaverse.workflow.model.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {

    // Find all attachments for a specific ticket
    List<TicketAttachment> findByTicket_Id(Long ticketId);
}


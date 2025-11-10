package com.metaverse.workflow.ticketSystem.repository;


import com.metaverse.workflow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Find tickets assigned to a specific user
    List<Ticket> findByAssignee_UserId(String userId);

    // Find tickets by status (e.g. "OPEN")
    List<Ticket> findByStatus(String status);

    // Find tickets by priority (e.g. "HIGH")
    List<Ticket> findByPriority(String priority);

    // Find by title (case-insensitive)
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);

    List<Ticket> findByReporter_UserId(String reporterId);
}


package com.metaverse.workflow.ticketSystem.repository;


import com.metaverse.workflow.model.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {}


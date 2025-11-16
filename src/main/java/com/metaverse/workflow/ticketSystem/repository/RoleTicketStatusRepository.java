package com.metaverse.workflow.ticketSystem.repository;

import com.metaverse.workflow.model.RoleTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleTicketStatusRepository extends JpaRepository<RoleTicketStatus, Long> {

    List<RoleTicketStatus> findByRoleName(String roleName);
}

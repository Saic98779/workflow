package com.metaverse.workflow.programstatus.repository;

import com.metaverse.workflow.programstatus.entity.ProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramStatusRepository extends JpaRepository<ProgramStatus, Long> {
}


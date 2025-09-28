package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramReschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRescheduleRepository extends JpaRepository<ProgramReschedule, Long> {
    List<ProgramReschedule> findByProgram_ProgramId(Long programId);

    @Query("SELECT pr.program.programId FROM ProgramReschedule pr")
    List<Long> findAllProgramIds();
}

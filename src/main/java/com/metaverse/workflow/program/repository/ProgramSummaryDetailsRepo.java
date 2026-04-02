package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramSummaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ProgramSummaryDetailsRepo extends JpaRepository<ProgramSummaryDetails, Long> {

    Optional<ProgramSummaryDetails> findByProgramProgramId(Long programId);
}
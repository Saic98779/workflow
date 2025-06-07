package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramOutcomeTableRepository extends JpaRepository<ProgramOutcomeTable, Integer> {
}

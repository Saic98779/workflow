package com.metaverse.workflow.ProgramMonitoring.repository;

import com.metaverse.workflow.model.JDApprovals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JDApprovalsRepository extends JpaRepository<JDApprovals, Long> {
}

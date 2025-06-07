package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.CGTMSETransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CGTMSETransactionRepository extends JpaRepository<CGTMSETransaction,Long> {
}

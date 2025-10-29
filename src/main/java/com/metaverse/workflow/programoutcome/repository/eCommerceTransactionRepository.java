package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.eCommerceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface eCommerceTransactionRepository extends JpaRepository<eCommerceTransaction,Long> {
}

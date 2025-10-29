package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
}

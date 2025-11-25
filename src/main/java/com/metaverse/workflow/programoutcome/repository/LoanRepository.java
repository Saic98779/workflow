package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByAgencyAgencyId(Long agencyId);
    long countByAgencyAgencyIdAndDateOfFirstDisbursementBetween(Long agencyId, Date start, Date end);
    long countByDateOfFirstDisbursementBetween(Date start, Date end);

    default long countLoan(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateOfFirstDisbursementBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfFirstDisbursementBetween(agencyId, start, end);
        }
    }

    Page<Loan> findByAgency_AgencyId(Long agencyId, Pageable pageable);

    long countByAgency_AgencyId(Long agencyId);
}

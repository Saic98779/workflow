package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMEGP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PMEGPRepository extends JpaRepository<PMEGP,Long> {
    default long countPMEGP(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByLoanSanctionedDateBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndLoanSanctionedDateBetween(agencyId, start, end);
        }
    }

    long countByAgencyAgencyIdAndLoanSanctionedDateBetween(Long agencyId, Date start, Date end);

    long countByLoanSanctionedDateBetween(Date start, Date start1);
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PMSRepository extends JpaRepository<PMS,Long> {

    default long countPMS(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateOfLoanReleasedBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfLoanReleasedBetween(agencyId, start, end);
        }
    }

    long countByDateOfLoanReleasedBetween(Date start, Date end);

    long countByAgencyAgencyIdAndDateOfLoanReleasedBetween(Long agencyId, Date start, Date end);
}

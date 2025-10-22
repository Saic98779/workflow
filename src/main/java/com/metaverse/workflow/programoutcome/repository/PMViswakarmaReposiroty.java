package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMViswakarma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PMViswakarmaReposiroty extends JpaRepository<PMViswakarma,Long> {
    default long countPMViswakarma(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateOfCreditAvailedBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfCreditAvailedBetween(agencyId, start, end);
        }
    }

    long countByDateOfCreditAvailedBetween(Date start, Date end);

    long countByAgencyAgencyIdAndDateOfCreditAvailedBetween(Long agencyId, Date start, Date end);

    List<PMViswakarma> findByAgencyAgencyId(Long agencyId);
}

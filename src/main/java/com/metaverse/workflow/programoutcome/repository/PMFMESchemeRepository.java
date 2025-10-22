package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMFMEScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PMFMESchemeRepository extends JpaRepository<PMFMEScheme,Long> {
    default long countPMFMEScheme(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateOfApplicationSubmissionBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfApplicationSubmissionBetween(agencyId, start, end);
        }
    }

    long countByAgencyAgencyIdAndDateOfApplicationSubmissionBetween(Long agencyId, Date start, Date end);

    long countByDateOfApplicationSubmissionBetween(Date start, Date start1);

    List<PMFMEScheme> findByAgencyAgencyId(Long agencyId);
}

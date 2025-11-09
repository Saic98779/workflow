package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.TreadMark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TreadMarkRepository extends JpaRepository<TreadMark,Long> {
    long countByAgencyAgencyIdAndDateOfRegistrationBetween(Long agencyId, Date dQ1Start, Date dQ1End);
    long countByDateOfRegistrationBetween( Date dQ1Start, Date dQ1End);

    default long countTreadMark(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByDateOfRegistrationBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfRegistrationBetween(agencyId, dQ1Start, dQ1End);
        }

    }

    List<TreadMark> findByAgencyAgencyId(Long agencyId);

    Page<TreadMark> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

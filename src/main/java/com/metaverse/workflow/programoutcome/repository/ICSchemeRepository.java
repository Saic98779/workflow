package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ICScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ICSchemeRepository extends JpaRepository<ICScheme,Long> {
    long countByAgencyAgencyIdAndCreatedOnBetween(Long agencyId, Date dQ1Start, Date dQ1End);
    long countByCreatedOnBetween(Date dQ1Start, Date dQ1End);


    default long countICScheme(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByCreatedOnBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndCreatedOnBetween(agencyId, dQ1Start, dQ1End);
        }

    }

    List<ICScheme> findByAgencyAgencyId(Long agencyId);
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ICScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ICSchemeRepository extends JpaRepository<ICScheme,Long> {
    long countByAgencyAgencyIdAndDateOfExportBetween(Long agencyId, Date dQ1Start, Date dQ1End);
    long countByDateOfExportBetween(Date dQ1Start, Date dQ1End);

    default long countICScheme(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByDateOfExportBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfExportBetween(agencyId, dQ1Start, dQ1End);
        }

    }

}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GIProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface GIProductRepository extends JpaRepository<GIProduct,Long> {
    long countByAgencyAgencyIdAndDateOfGIRegistrationBetween(Long agencyId, Date dQ1Start, Date dQ1End);
    long countByDateOfGIRegistrationBetween(Date dQ1Start, Date dQ1End);

    default long countGIProduct(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByDateOfGIRegistrationBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfGIRegistrationBetween(agencyId, dQ1Start, dQ1End);
        }

    }
}

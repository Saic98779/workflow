package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMMY;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PMMYRepository extends JpaRepository<PMMY, Long> {
    default long countPMMY(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByGroundingDateBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndGroundingDateBetween(agencyId, start, end);
        }
    }

    long countByAgencyAgencyIdAndGroundingDateBetween(Long agencyId, Date start, Date end);
    long countByGroundingDateBetween(Date start, Date start1);

    List<PMMY> findByAgencyAgencyId(Long agencyId);

    Page<PMMY> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.SIDBIAspire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SIDBIAspireRepository extends JpaRepository<SIDBIAspire,Long> {
    default long countSIDBIAspire(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateSanctionUnderAspireBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateSanctionUnderAspireBetween(agencyId, start, end);
        }
    }

    long countByDateSanctionUnderAspireBetween(Date start, Date end);

    long countByAgencyAgencyIdAndDateSanctionUnderAspireBetween(Long agencyId, Date start, Date end);

    List<SIDBIAspire> findByAgencyAgencyId(Long agencyId);

    Page<SIDBIAspire> findByAgency_AgencyId(Long agencyId, Pageable pageable);

    long countByAgency_AgencyId(Long agencyId);
}

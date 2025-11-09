package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.Patents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PatentsRepository extends JpaRepository<Patents,Long> {
    long countByAgencyAgencyIdAndDateOfExportBetween(Long agencyId, Date dQ1Start, Date dQ1End);
    long countByDateOfExportBetween(Date dQ1Start, Date dQ1End);

    default long countPatents(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByDateOfExportBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfExportBetween(agencyId, dQ1Start, dQ1End);
        }

    }

    List<Patents> findByAgencyAgencyId(Long agencyId);

    Page<Patents> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

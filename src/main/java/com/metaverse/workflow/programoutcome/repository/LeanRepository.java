package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.Lean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LeanRepository extends JpaRepository<Lean,Long> {


    default long countLean(Long agencyId, Date start, Date end){
        if (agencyId == -1) {
            return countByDateOfCertificationBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfCertificationBetween(agencyId, start, end);
        }
    }

    long countByAgencyAgencyIdAndDateOfCertificationBetween(Long agencyId, Date start, Date end);

    long countByDateOfCertificationBetween(Date start, Date end);

    List<Lean> findByAgencyAgencyId(Long agencyId);

    Page<Lean> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.DesignRights;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DesignRightsRepository extends JpaRepository<DesignRights,Long> {
    long countByAgencyAgencyIdAndDateOfApplicationBetween(Long agencyId, Date start, Date end);
    long countByDateOfApplicationBetween(Date start, Date end);
    default long countDesignRights(Long agencyId, Date start, Date end){
        if (agencyId == -1) {
            return countByDateOfApplicationBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfApplicationBetween(agencyId, start, end);
        }
    }

    List<DesignRights> findByAgencyAgencyId(Long agencyId);

    Page<DesignRights> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

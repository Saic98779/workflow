package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.CopyRights;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CopyRightsRepository extends JpaRepository<CopyRights,Long> {
    long countByAgencyAgencyIdAndDateOfApplicationFiledBetween(Long agencyId, Date start, Date end);
    long countByDateOfApplicationFiledBetween(Date start, Date end);
   default long countCopyRights(Long agencyId, Date start, Date end){
       if (agencyId == -1) {
           return countByDateOfApplicationFiledBetween(start, start);
       } else if (start == null || end == null) {
           return count();
       } else {
           return countByAgencyAgencyIdAndDateOfApplicationFiledBetween(agencyId, start, end);
       }
   }

    List<CopyRights> findByAgencyAgencyId(Long agencyId);

    Page<CopyRights> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

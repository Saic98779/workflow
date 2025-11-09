package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.OEM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OEMRepository extends JpaRepository<OEM,Long> {
     long countByAgencyAgencyIdAndOemRegistrationDateBetween(Long agencyId,Date start,Date end);
    long countByOemRegistrationDateBetween(Date start,Date end);
     default long countOEM(Long agencyId, Date start, Date end){
         if (agencyId == -1) {
             return countByOemRegistrationDateBetween(start, start);
         } else if (start == null || end == null) {
             return count();
         } else {
             return countByAgencyAgencyIdAndOemRegistrationDateBetween(agencyId, start, end);
         }
    }

    List<OEM> findByAgencyAgencyId(Long agencyId);

    Page<OEM> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.VendorDevelopment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VendorDevelopmentRepository extends JpaRepository<VendorDevelopment,Long> {
     default  long countVendorDevelopment(Long agencyId, Date start, Date end){
        if (agencyId == -1) {
            return countByDateOfParticipationBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfParticipationBetween(agencyId, start, end);
        }
    }

    long countByAgencyAgencyIdAndDateOfParticipationBetween(Long agencyId, Date start, Date end);
    long countByDateOfParticipationBetween(Date start, Date start1);

    List<VendorDevelopment> findByAgencyAgencyId(Long agencyId);
}

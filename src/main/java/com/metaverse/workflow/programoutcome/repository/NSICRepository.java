package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.NSIC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NSICRepository extends JpaRepository<NSIC,Long> {
    long countByAgencyAgencyIdAndDateOfProcurementBetween(Long agencyId, Date start, Date end);
    long countByDateOfProcurementBetween(Date start, Date end);
    default long countNSIC(Long agencyId, Date start, Date end){
        if (agencyId == -1) {
            return countByDateOfProcurementBetween(start, start);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfProcurementBetween(agencyId, start, end);
        }
    }

    List<NSIC> findByAgencyAgencyId(Long agencyId);
}

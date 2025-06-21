package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ConsortiaTender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ConsortiaTenderRepository  extends JpaRepository<ConsortiaTender,Long> {
    long countByAgencyAgencyIdAndDateOfJoiningConsortiaBetween(Long agencyId, Date start, Date end);
    long countByDateOfJoiningConsortiaBetween(Date start, Date end);
     default long countConsortiaTender(Long agencyId, Date start, Date end){
         if (agencyId == -1) {
             return countByDateOfJoiningConsortiaBetween(start, end);
         } else if (start == null || end == null) {
             return count();
         } else {
             return countByAgencyAgencyIdAndDateOfJoiningConsortiaBetween(agencyId, start, end);
         }
     }
}

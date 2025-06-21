package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.CGTMSETransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CGTMSETransactionRepository extends JpaRepository<CGTMSETransaction,Long> {
    long countByAgencyAgencyIdAndCreditApplicationDateBetween(Long agencyId, Date dQ2Start, Date dQ2End);
    long countByCreditApplicationDateBetween(Date dQ2Start, Date dQ2End);
   default long countCGTMSETransaction(Long agencyId, Date dQ1Start, Date dQ1End){
       if (agencyId == -1) {
           return countByCreditApplicationDateBetween(dQ1Start, dQ1End);
       } else if (dQ1Start == null || dQ1End == null) {
           return count();
       } else {
           return countByAgencyAgencyIdAndCreditApplicationDateBetween(agencyId, dQ1Start, dQ1End);
       }
   }
}

package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GeMTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface GeMTransactionRepository extends JpaRepository<GeMTransaction,Long> {
    List<GeMTransaction> findByOndcRegistration_Agency_AgencyId(Long agencyId);
//    List<GeMTransaction> findByAgencyAgencyId(Long agencyId);

//   default long countGeMTransaction(Long agencyId, Date start, Date end){
//       if (agencyId == -1) {
//           return countByProcurementDateBetween(start, end);
//       } else if (start == null || end == null) {
//           return count();
//       } else {
//           return countByAgencyAgencyIdAndProcurementDateBetween(agencyId, start, end);
//       }
//   }
//
//    long countByProcurementDateBetween(Date start, Date end);
//
//    long countByAgencyAgencyIdAndProcurementDateBetween(Long agencyId, Date start, Date end);
}

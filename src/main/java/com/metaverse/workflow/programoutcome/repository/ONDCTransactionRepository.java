package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ONDCRegistration;
import com.metaverse.workflow.model.outcomes.ONDCTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ONDCTransactionRepository extends JpaRepository<ONDCTransaction, Long> {
    @Query("SELECT COUNT(t) FROM ONDCTransaction t WHERE t.ondcRegistration.agency.agencyId = :agencyId AND t.transactionDate BETWEEN :startDate AND :endDate")
    long countByAgencyAndTransactionDateBetween(@Param("agencyId") Long agencyId,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);

    @Query("SELECT COUNT(t) FROM ONDCTransaction t WHERE  t.transactionDate BETWEEN :startDate AND :endDate")
    long countByTransactionDateBetween(@Param("startDate") Date startDate,
                                       @Param("endDate") Date endDate);

    default long countONDCTransaction(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByTransactionDateBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAndTransactionDateBetween(agencyId, dQ1Start, dQ1End);
        }

    }

    List<ONDCTransaction> findByOndcRegistration_Agency_AgencyId(Long agencyId);

    Page<ONDCTransaction> findByOndcRegistration_Agency_AgencyId(Long agencyId, Pageable pageable);


//    List<ONDCTransaction> findByAgencyAgencyId(Long agencyId);
}

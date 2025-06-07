package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.TReDSTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TReDSTransactionRepository extends JpaRepository<TReDSTransaction,Long> {
    @Query("SELECT COUNT(t) FROM TReDSTransaction t WHERE t.tredsRegistration.agency.agencyId = :agencyId AND t.tredsTransactionDate BETWEEN :startDate AND :endDate")
    long countByAgencyAndTransactionDateBetween(@Param("agencyId") Long agencyId,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);

    @Query("SELECT COUNT(t) FROM TReDSTransaction t WHERE t.tredsTransactionDate BETWEEN :startDate AND :endDate")
    long countByTransactionDateBetween(@Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);

    default long countTReDSTransaction(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByTransactionDateBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAndTransactionDateBetween(agencyId, dQ1Start, dQ1End);
        }

    }
}

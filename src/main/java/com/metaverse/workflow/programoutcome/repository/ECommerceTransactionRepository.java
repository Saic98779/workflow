package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ECommerceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ECommerceTransactionRepository extends JpaRepository<ECommerceTransaction,Long> {
    List<ECommerceTransaction> findByEcommerceRegistration_Agency_AgencyId(Long agencyId);
    long countByEcommerceRegistration_Agency_AgencyIdAndCreatedOnBetween(Long agencyId, Date start, Date end);
    long countByCreatedOnBetween(Date start, Date end);

    default long countECommerceTransaction(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByCreatedOnBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByEcommerceRegistration_Agency_AgencyIdAndCreatedOnBetween(agencyId, start, end);
        }
    }
}

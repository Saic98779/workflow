package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ExportPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExportPromotionRepository extends JpaRepository<ExportPromotion,Long> {
    List<ExportPromotion> findByAgencyAgencyId(Long agencyId);
    long countByAgencyAgencyIdAndExportDateBetween(Long agencyId, Date start, Date end);
    long countByExportDateBetween(Date start, Date end);

    default long countExportPromotion(Long agencyId, Date start, Date end) {
        return 0;
    }

    Page<ExportPromotion> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

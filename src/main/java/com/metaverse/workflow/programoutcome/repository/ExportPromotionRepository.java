package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ExportPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ExportPromotionRepository extends JpaRepository<ExportPromotion,Long> {
    List<ExportPromotion> findByAgencyAgencyId(Long agencyId);
    long countByAgencyAgencyIdAndExportDateBetween(Long agencyId, Date start, Date end);
    long countByExportDateBetween(Date start, Date end);

    default long countExportPromotion(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByExportDateBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndExportDateBetween(agencyId, start, end);
        }
    }
}

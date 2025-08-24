package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.outcomes.ONDCRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutcomeRepository extends JpaRepository<ONDCRegistration, Long> {

    @Query(value = """
        SELECT SUM(cnt) AS total_count
        FROM (
            SELECT COUNT(*) AS cnt FROM outcome_ondc_registration WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_udyam_registration WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_cgtmse_transaction WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_gem_transaction WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_treds_registration WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_pmmy WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_pms WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_ic_scheme WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_nisc WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_patents WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_gi_products WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_barcode WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_tread_mark WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_zed_certification WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_lean WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_pm_viswakarma WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_vendor_development WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_sc_st_hub WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_sidbi_aspire WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_gem_registration WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_oem WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_pmfme_scheme WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_consortia_tender WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_design_rights WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_copy_rights WHERE agency_id = :agencyId
            UNION ALL
            SELECT COUNT(*) FROM outcome_greening_of_msme WHERE agency_id = :agencyId
        ) AS counts
        """, nativeQuery = true)
    Long getTotalOutcomesByAgency(@Param("agencyId") Long agencyId);
}

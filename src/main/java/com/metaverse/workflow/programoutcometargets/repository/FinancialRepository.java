package com.metaverse.workflow.programoutcometargets.repository;

import com.metaverse.workflow.model.FinancialTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialRepository extends JpaRepository<FinancialTarget,Long> {
    List<FinancialTarget> findByAgencyAgencyId(Long agencyId);

    @Query(value = """
        SELECT DISTINCT 
            ft.financial_target_id AS financialTargetId,
            ag.agency_name AS agencyName,
            ac.activity_name AS activityName,
            ft.financial_year AS financialYear,
            SUM(ft.q1)/2 AS q1,
            SUM(ft.q2)/2 AS q2,
            SUM(ft.q3)/2 AS q3,
            SUM(ft.q4)/2 AS q4,
            SUM(ft.q1 + ft.q2 + ft.q3 + ft.q4)/2 AS yearlyTarget
        FROM murthy_workflow.financial_target ft
        LEFT JOIN murthy_workflow.activity ac
            ON ft.agency_id = ac.agency_id
        LEFT JOIN murthy_workflow.agency ag
            ON ft.agency_id = ag.agency_id
        WHERE ft.agency_id = :agencyId
        GROUP BY ft.financial_year
        """, nativeQuery = true)
    List<Object[]> getFinancialTargetSummary(@Param("agencyId") Long agencyId);
}

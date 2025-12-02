package com.metaverse.workflow.MoMSMEReport.repository;

import com.metaverse.workflow.model.MoMSMEReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MoMSMEReportRepo extends JpaRepository<MoMSMEReport,Long> {

    // Return distinct intervention names
    @Query("SELECT DISTINCT r.intervention FROM MoMSMEReport r ORDER BY r.intervention")
    List<String> findDistinctInterventions();

    // Return distinct components for a given intervention
    @Query("SELECT DISTINCT r.component FROM MoMSMEReport r WHERE r.intervention = :intervention ORDER BY r.component")
    List<String> findDistinctComponentsByIntervention(@Param("intervention") String intervention);

    // Return distinct activity name and corresponding moMSMEActivityId for a given component
    @Query("SELECT DISTINCT r.activity, r.moMSMEActivityId FROM MoMSMEReport r WHERE r.component = :component ORDER BY r.activity")
    List<Object[]> findDistinctActivityAndMoMSMEActivityIdByComponent(@Param("component") String component);

    List<MoMSMEReport> findByIntervention(String intervention);
}

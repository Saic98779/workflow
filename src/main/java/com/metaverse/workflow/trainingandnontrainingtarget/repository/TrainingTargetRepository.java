package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.SubActivity;
import com.metaverse.workflow.model.TrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrainingTargetRepository extends JpaRepository<TrainingTargets, Long> {

    // fetch by agency and year
    List<TrainingTargets> findByAgency_AgencyIdAndFinancialYear(Long agencyId, String financialYear);

    // fetch all by agency
    List<TrainingTargets> findByAgency_AgencyId(Long agencyId);
    List<TrainingTargets> findBySubActivity_SubActivityIdInAndAgency_AgencyId(Iterable<Long> subActivityId, Long agencyId);

    List<TrainingTargets> findBySubActivity_SubActivityIdIn(Set<Long> subActivityIds);

    List<TrainingTargets> findBySubActivity_SubActivityId(Long subActivityId);

    Optional<TrainingTargets> findByAgencyAndSubActivityAndFinancialYear(Agency agency, SubActivity subActivity, String financialYear);

    @Query("""
   SELECT t FROM TrainingTargets t
   WHERE t.agency.agencyId = :agencyId
     AND (
            (:q1 = true AND t.q1Target IS NOT NULL) OR
            (:q2 = true AND t.q2Target IS NOT NULL) OR
            (:q3 = true AND t.q3Target IS NOT NULL) OR
            (:q4 = true AND t.q4Target IS NOT NULL)
         )
""")
    List<TrainingTargets> findTargetsByAgencyAndQuarterRange(
            @Param("agencyId") Long agencyId,
            @Param("q1") boolean q1,
            @Param("q2") boolean q2,
            @Param("q3") boolean q3,
            @Param("q4") boolean q4
    );
    @Query("""
    SELECT t FROM TrainingTargets t
    WHERE 
        (:q1 = true AND t.q1Target IS NOT NULL) OR
        (:q2 = true AND t.q2Target IS NOT NULL) OR
        (:q3 = true AND t.q3Target IS NOT NULL) OR
        (:q4 = true AND t.q4Target IS NOT NULL)
""")
    List<TrainingTargets> findTargetsByQuarterRange(
            @Param("q1") boolean q1,
            @Param("q2") boolean q2,
            @Param("q3") boolean q3,
            @Param("q4") boolean q4
    );


}


package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.NonTrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NonTrainingTargetRepository extends JpaRepository<NonTrainingTargets, Long> {

    List<NonTrainingTargets> findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(Long agencyId);
    List<NonTrainingTargets> findByNonTrainingSubActivity_subActivityId(Long subActivityId);


    @Query("SELECT tt " +
            "FROM NonTrainingTargets tt " +
            "WHERE tt.nonTrainingSubActivity.nonTrainingActivity.agency.agencyId = :agencyId " +
            "AND tt.financialYear = :financialYear")
    List<NonTrainingTargets> findByAgencyAndFinancialYear(
            @Param("agencyId") Long agencyId,
            @Param("financialYear") String financialYear);

    List<NonTrainingTargets> findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyIdAndFinancialYear(
            Long agencyId, String financialYear
    );
    List<NonTrainingTargets> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);

    Optional<NonTrainingTargets> findByNonTrainingSubActivityAndFinancialYear(NonTrainingSubActivity subActivity, String financialYear);
}


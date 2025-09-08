package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.model.TrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingTargetRepository extends JpaRepository<NonTrainingTargets, Long> {

    List<NonTrainingTargets> findByNonTrainingSubActivity_NonTrainingActivity_Agency_AgencyId(Long agencyId);


    @Query("SELECT tt " +
            "FROM NonTrainingTargets tt " +
            "WHERE tt.nonTrainingSubActivity.nonTrainingActivity.agency.agencyId = :agencyId " +
            "AND tt.financialYear = :financialYear")
    List<NonTrainingTargets> findByAgencyAndFinancialYear(
            @Param("agencyId") Long agencyId,
            @Param("financialYear") String financialYear);
}


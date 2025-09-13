package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingExpenditure;
import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NonTrainingExpenditureRepository extends JpaRepository<NonTrainingExpenditure,Long> {

    @Query("SELECT nt.nonTrainingSubActivity.subActivityId, SUM(nt.expenditureAmount) " +
            "FROM NonTrainingExpenditure nt " +
            "WHERE nt.agency.agencyId = :agencyId " +
            "GROUP BY nt.nonTrainingSubActivity.subActivityId")
    List<Object[]> sumExpenditureByAgencyGroupedBySubActivity(@Param("agencyId") Long agencyId);

    Optional<List<NonTrainingExpenditure>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);

    @Query("SELECT SUM(pe.expenditureAmount) " +
            "FROM NonTrainingExpenditure pe " +
            "WHERE pe.agency.agencyId = :agencyId " +
            "AND pe.nonTrainingSubActivity.subActivityId = :subActivityId " +
            "AND pe.billDate BETWEEN :startDate AND :endDate")
    Double sumExpenditureByAgencyAndSubActivityAndDateRange(
            @Param("agencyId") Long agencyId,
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query(value = """
    SELECT COUNT(nte.id)
    FROM non_training_expenditure nte
    JOIN non_training_sub_activity ntsa ON nte.sub_activity_id = ntsa.sub_activity_id
    WHERE ntsa.sub_activity_id = :subActivityId
      AND nte.created_on BETWEEN :startDate AND :endDate
""", nativeQuery = true)
    Long countRegistrationsBySubActivityAndDateRange(
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}

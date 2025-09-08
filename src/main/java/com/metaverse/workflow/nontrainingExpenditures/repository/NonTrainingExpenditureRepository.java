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

    @Query("""
        SELECT 
            pe.nonTrainingSubActivity.subActivityId as subActivityId,
            CASE 
                WHEN MONTH(pe.billDate) BETWEEN 4 AND 6 THEN 1
                WHEN MONTH(pe.billDate) BETWEEN 7 AND 9 THEN 2
                WHEN MONTH(pe.billDate) BETWEEN 10 AND 12 THEN 3
                ELSE 4
            END as quarter,
            SUM(pe.expenditureAmount)
        FROM NonTrainingExpenditure pe
        WHERE pe.agency.agencyId = :agencyId
          AND pe.billDate BETWEEN :startDate AND :endDate
        GROUP BY pe.nonTrainingSubActivity.subActivityId, quarter
    """)
    List<Object[]> sumExpenditureBySubActivityAndQuarter(
            @Param("agencyId") Long agencyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

}

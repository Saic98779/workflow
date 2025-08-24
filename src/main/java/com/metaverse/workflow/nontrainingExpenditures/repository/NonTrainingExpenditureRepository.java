package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NonTrainingExpenditureRepository extends JpaRepository<NonTrainingExpenditure,Long> {
    Optional<List<NonTrainingExpenditure>> findByNonTrainingActivity_ActivityId(Long nonTrainingActivityId);

    @Query("SELECT SUM(nte.expenditureAmount) FROM NonTrainingExpenditure nte " +
            "WHERE nte.agency.agencyId = :agencyId " +
            "AND nte.nonTrainingActivity.activityId = :activityId " +
            "AND nte.paymentDate BETWEEN :startDate AND :endDate")
    Double sumExpenditureByAgencyAndActivityAndDateRange(@Param("agencyId") Long agencyId,
                                                         @Param("activityId") Long activityId,
                                                         @Param("startDate") Date startDate,
                                                         @Param("endDate") Date endDate);

}

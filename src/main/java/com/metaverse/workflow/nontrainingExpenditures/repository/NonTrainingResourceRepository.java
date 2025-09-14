package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface NonTrainingResourceRepository extends JpaRepository<NonTrainingResource, Long> {

    @Query("SELECT COALESCE(SUM(ntrEx.amount), 0) " +
            "FROM NonTrainingResource ntr " +
            "JOIN ntr.nonTrainingResourceExpenditures ntrEx " +
            "JOIN ntr.nonTrainingSubActivity ntsa " +
            "JOIN ntsa.nonTrainingActivity nta " +
            "WHERE nta.activityId = :activityId " +
            "AND ntsa.subActivityId = :subActivityId")
    Double sumExpenditureByActivityAndSubActivity(@Param("activityId") Long activityId,
                                                  @Param("subActivityId") Long subActivityId);


    @Query("SELECT SUM(nre.amount) " +
            "FROM NonTrainingResourceExpenditure nre " +
            "JOIN nre.nonTrainingResource nr " +
            "JOIN nr.nonTrainingSubActivity ntsa " +
            "JOIN ntsa.nonTrainingActivity nta " +
            "JOIN nta.agency a " +
            "WHERE a.agencyId = :agencyId " +
            "AND ntsa.subActivityId = :subActivityId " +
            "AND nre.dateOfPayment BETWEEN :startDate AND :endDate")
    Double sumExpenditureByAgencyAndSubActivityAndDateRange(
            @Param("agencyId") Long agencyId,
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    List<NonTrainingResource> findByNonTrainingSubActivity_subActivityId(Long subActivityId);


    @Query(value = """
    SELECT COUNT(ntr.resource_id)
    FROM non_training_resources ntr
    JOIN non_training_sub_activity ntsa ON ntr.sub_activity_id = ntsa.sub_activity_id
    WHERE ntsa.sub_activity_id = :subActivityId
      AND ntr.created_on BETWEEN :startDate AND :endDate
""", nativeQuery = true)
    Long countResourcesBySubActivityAndDateRange(
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


}


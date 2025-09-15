package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.TravelAndTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TravelAndTransportRepository extends JpaRepository<TravelAndTransport, Long> {
    List<TravelAndTransport> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM TravelAndTransport t " +
            "JOIN t.nonTrainingSubActivity ntsa " +
            "JOIN ntsa.nonTrainingActivity nta " +
            "WHERE nta.activityId = :activityId " +
            "AND ntsa.subActivityId = :subActivityId")
    Double sumTravelAndTransportByActivityAndSubActivity(@Param("activityId") Long activityId,
                                                         @Param("subActivityId") Long subActivityId);


    @Query("SELECT SUM(tt.amount) " +
            "FROM TravelAndTransport tt " +
            "WHERE tt.nonTrainingSubActivity.subActivityId = :subActivityId " +
            "AND tt.nonTrainingSubActivity.nonTrainingActivity.agency.agencyId = :agencyId " +
            "AND tt.billDate BETWEEN :startDate AND :endDate")
    Double sumExpenditureByAgencyAndSubActivityAndDateRange(
            @Param("agencyId") Long agencyId,
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT COUNT(tt.travel_transport_id)
    FROM travel_and_transport tt
    JOIN non_training_sub_activity ntsa ON tt.sub_activity_id = ntsa.sub_activity_id
    WHERE ntsa.sub_activity_id = :subActivityId
      AND tt.date_of_travel BETWEEN :startDate AND :endDate
""", nativeQuery = true)
    Long countTravelBySubActivityAndDateRange(
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}

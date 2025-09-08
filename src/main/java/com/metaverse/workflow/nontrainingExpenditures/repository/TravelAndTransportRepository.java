package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.TravelAndTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}

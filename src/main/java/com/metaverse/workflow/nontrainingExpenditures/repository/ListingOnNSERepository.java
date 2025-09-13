package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.ListingOnNSE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ListingOnNSERepository extends JpaRepository<ListingOnNSE,Long> {
    Optional<List<ListingOnNSE>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);

    @Query(value = """
    SELECT COUNT(l.listing_on_nse_id)
    FROM listing_on_nse l
    JOIN non_training_sub_activity ntsa ON l.sub_activity_id = ntsa.sub_activity_id
    WHERE ntsa.sub_activity_id = :subActivityId
      AND l.created_on BETWEEN :startDate AND :endDate
""", nativeQuery = true)
    Long countListingsBySubActivityAndDateRange(
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT SUM(l.amountOfLoanProvided) " +
            "FROM ListingOnNSE l " +
            "WHERE l.nonTrainingSubActivity.subActivityId = :subActivityId " +
            "AND l.dateOfLoan BETWEEN :startDate AND :endDate")
    Double sumLoanAmountBySubActivityAndDateRange(
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

}

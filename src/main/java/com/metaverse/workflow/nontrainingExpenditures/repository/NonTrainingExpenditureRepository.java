package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingExpenditure;
import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NonTrainingExpenditureRepository extends JpaRepository<NonTrainingExpenditure,Long> {

    @Query("SELECT nt.nonTrainingSubActivity.subActivityId, SUM(nt.expenditureAmount) " +
            "FROM NonTrainingExpenditure nt " +
            "WHERE nt.agency.agencyId = :agencyId " +
            "GROUP BY nt.nonTrainingSubActivity.subActivityId")
    List<Object[]> sumExpenditureByAgencyGroupedBySubActivity(@Param("agencyId") Long agencyId);

    Optional<List<NonTrainingExpenditure>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

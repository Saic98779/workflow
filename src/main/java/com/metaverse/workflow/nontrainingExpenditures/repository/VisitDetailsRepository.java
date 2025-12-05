package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.VisitDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitDetailsRepository extends JpaRepository<VisitDetails, Long> {
    List<VisitDetails> findByNonTrainingSubActivitySubActivityId(Long subActivityId);

}

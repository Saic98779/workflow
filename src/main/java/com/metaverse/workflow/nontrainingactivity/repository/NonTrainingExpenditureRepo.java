package com.metaverse.workflow.nontrainingactivity.repository;

import com.metaverse.workflow.model.NonTrainingExpenditure;
import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NonTrainingExpenditureRepo extends JpaRepository<NonTrainingExpenditure,Long> {
    Optional<List<NonTrainingExpenditure>> findByNonTrainingActivity_ActivityId(Long nonTrainingActivityId);
}

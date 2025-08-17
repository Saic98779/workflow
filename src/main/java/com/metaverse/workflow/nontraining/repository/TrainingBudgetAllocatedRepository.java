package com.metaverse.workflow.nontraining.repository;

import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.TrainingBudgetAllocated;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingBudgetAllocatedRepository extends JpaRepository<TrainingBudgetAllocated,Long> {
    List<TrainingBudgetAllocated> findByAgency_AgencyId(Long agencyId);

}

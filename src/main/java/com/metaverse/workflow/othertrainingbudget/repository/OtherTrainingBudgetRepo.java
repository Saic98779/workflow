package com.metaverse.workflow.othertrainingbudget.repository;

import com.metaverse.workflow.model.OtherTrainingBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherTrainingBudgetRepo extends JpaRepository<OtherTrainingBudget,Long> {
    List<OtherTrainingBudget> findByAgency_AgencyId(Long agencyId);
}

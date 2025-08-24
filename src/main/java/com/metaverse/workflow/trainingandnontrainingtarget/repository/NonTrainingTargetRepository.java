package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.NonTrainingTargets;
import com.metaverse.workflow.model.TrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingTargetRepository extends JpaRepository<NonTrainingTargets, Long> {
    List<NonTrainingTargets> findByNonTrainingActivity_Agency_AgencyIdAndFinancialYear(Long agencyId, String financialYear);
}

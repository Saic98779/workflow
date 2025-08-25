package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.TrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTargetRepository extends JpaRepository<TrainingTargets, Long> {
    List<TrainingTargets> findByAgency_AgencyIdAndFinancialYear(Long agencyId, String financialYear);

    List<TrainingTargets> findByAgency_AgencyId(Long agencyId);

    List<TrainingTargets> findByActivity_Agency_AgencyId(Long agencyId);
}

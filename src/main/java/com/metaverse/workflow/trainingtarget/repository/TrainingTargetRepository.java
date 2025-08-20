package com.metaverse.workflow.trainingtarget.repository;

import com.metaverse.workflow.model.TrainingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTargetRepository extends JpaRepository<TrainingTarget, Long> {
    List<TrainingTarget> findByAgency_AgencyIdAndFinancialYear(Long agencyId, String financialYear);

    List<TrainingTarget> findByAgency_AgencyId(Long agencyId);
}

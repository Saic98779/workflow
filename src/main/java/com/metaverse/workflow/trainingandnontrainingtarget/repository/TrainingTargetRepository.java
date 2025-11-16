package com.metaverse.workflow.trainingandnontrainingtarget.repository;

import com.metaverse.workflow.model.TrainingTargets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TrainingTargetRepository extends JpaRepository<TrainingTargets, Long> {

    // fetch by agency and year
    List<TrainingTargets> findByAgency_AgencyIdAndFinancialYear(Long agencyId, String financialYear);

    // fetch all by agency
    List<TrainingTargets> findByAgency_AgencyId(Long agencyId);
    List<TrainingTargets> findBySubActivity_SubActivityIdInAndAgency_AgencyId(Iterable<Long> subActivityId, Long agencyId);

    List<TrainingTargets> findBySubActivity_SubActivityIdIn(Set<Long> subActivityIds);

    List<TrainingTargets> findBySubActivity_SubActivityId(Long subActivityId);
}


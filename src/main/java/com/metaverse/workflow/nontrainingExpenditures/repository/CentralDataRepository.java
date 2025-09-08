package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NIMSMECentralData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CentralDataRepository extends JpaRepository<NIMSMECentralData,Long> {
    Optional<List<NIMSMECentralData>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

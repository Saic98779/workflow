package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.AccessToTechnologyAndInfrastructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessToTechnologyAndInfrastructureRepository extends JpaRepository<AccessToTechnologyAndInfrastructure,Long> {
    List<AccessToTechnologyAndInfrastructure> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.BusinessPlanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessPlanDetailsRepository extends JpaRepository<BusinessPlanDetails, Long> {
    List<BusinessPlanDetails> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);

}


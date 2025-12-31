package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.CreditCounselling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCounsellingRepository extends JpaRepository<CreditCounselling,Long> {
    List<CreditCounselling> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.AccessToFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessToFinanceRepository extends JpaRepository<AccessToFinance,Long> {
    List<AccessToFinance> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);

}

package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovtSchemeFinanceRepository extends JpaRepository<GovtSchemeFinance, Long> {

    List<GovtSchemeFinance>
    findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

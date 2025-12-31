package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.MarketStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketStudyRepository extends JpaRepository<MarketStudy, Long> {

    List<MarketStudy> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.AccessToPackagingLabellingAndBranding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessToPackagingLabellingAndBrandingRepository extends JpaRepository<AccessToPackagingLabellingAndBranding,Long> {
    List<AccessToPackagingLabellingAndBranding> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);

}

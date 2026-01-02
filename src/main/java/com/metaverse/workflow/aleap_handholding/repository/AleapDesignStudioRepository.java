package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.AleapDesignStudio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AleapDesignStudioRepository extends JpaRepository<AleapDesignStudio, Long> {
    List<AleapDesignStudio> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

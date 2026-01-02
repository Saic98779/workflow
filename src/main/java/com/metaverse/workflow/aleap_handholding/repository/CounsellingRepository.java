package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.Counselling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CounsellingRepository extends JpaRepository<Counselling, Long> {
    List<Counselling> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);

}


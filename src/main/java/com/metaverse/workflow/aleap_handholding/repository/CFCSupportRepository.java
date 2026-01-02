package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.CFCSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CFCSupportRepository extends JpaRepository<CFCSupport, Long> {

    List<CFCSupport> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

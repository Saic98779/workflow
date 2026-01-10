package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.TestingQualityCertificationSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestingQualityCertificationSupportRepository extends
        JpaRepository<TestingQualityCertificationSupport,Long> {

    List<TestingQualityCertificationSupport> findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

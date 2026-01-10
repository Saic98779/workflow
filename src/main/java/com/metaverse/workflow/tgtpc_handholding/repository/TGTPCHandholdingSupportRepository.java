package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TGTPCHandholdingSupportRepository extends JpaRepository<TGTPCHandholdingSupport,Long> {
    List<TGTPCHandholdingSupport> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

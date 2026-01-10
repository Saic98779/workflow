package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.RawMaterialSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawMaterialSupportRepository extends JpaRepository<RawMaterialSupport,Long> {
    List<RawMaterialSupport> findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

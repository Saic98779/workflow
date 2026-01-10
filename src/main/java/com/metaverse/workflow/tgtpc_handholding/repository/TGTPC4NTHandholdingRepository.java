package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.TGTPC4NTHandholding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TGTPC4NTHandholdingRepository extends JpaRepository<TGTPC4NTHandholding,Long> {
    List<TGTPC4NTHandholding> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

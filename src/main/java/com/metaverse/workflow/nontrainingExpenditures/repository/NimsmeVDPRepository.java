package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NimsmeVDP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NimsmeVDPRepository extends JpaRepository<NimsmeVDP,Long> {
    List<NimsmeVDP> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

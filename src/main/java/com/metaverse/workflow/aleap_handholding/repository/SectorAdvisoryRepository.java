package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.SectorAdvisory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorAdvisoryRepository
        extends JpaRepository<SectorAdvisory, Long> {
    List<SectorAdvisory> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

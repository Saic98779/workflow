package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.WeHubHandholding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeHubHandholdingRepository extends JpaRepository<WeHubHandholding,Long> {
    List<WeHubHandholding> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

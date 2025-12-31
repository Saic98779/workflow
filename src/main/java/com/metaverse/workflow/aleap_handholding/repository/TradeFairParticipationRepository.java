package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.TradeFairParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeFairParticipationRepository extends JpaRepository<TradeFairParticipation, Long> {
    List<TradeFairParticipation> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}

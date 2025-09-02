package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.TravelAndTransport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelAndTransportRepository extends JpaRepository<TravelAndTransport, Long> {
    List<TravelAndTransport> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

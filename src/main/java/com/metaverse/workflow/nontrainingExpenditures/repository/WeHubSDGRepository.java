package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.WeHubSDG;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeHubSDGRepository extends JpaRepository<WeHubSDG,Long> {
    Optional<List<WeHubSDG>> findByNonTrainingSubActivity_SubActivityId(Long nonTrainingActivityId);
}

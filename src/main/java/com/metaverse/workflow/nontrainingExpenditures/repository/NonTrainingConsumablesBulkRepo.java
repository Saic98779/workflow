package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingConsumablesBulk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingConsumablesBulkRepo extends JpaRepository<NonTrainingConsumablesBulk, Long> {

    List<NonTrainingConsumablesBulk> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}


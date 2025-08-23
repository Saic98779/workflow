package com.metaverse.workflow.nontrainingactivity.repository;

import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepo extends JpaRepository<NonTrainingResource,Long> {
    Optional<List<NonTrainingResource>> findByNonTrainingActivity_ActivityId(Long activityId);
}

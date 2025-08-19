package com.metaverse.workflow.nontrainingactivity.repository;

import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepo extends JpaRepository<NonTrainingResource,Long> {
}

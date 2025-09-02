package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingResource;
import com.metaverse.workflow.model.NonTrainingResourceExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingResourceRepository extends JpaRepository<NonTrainingResource,Long> {
    List<NonTrainingResource> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

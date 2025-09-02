package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingResourceExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NonTrainingResourceExpenditureRepo  extends JpaRepository<NonTrainingResourceExpenditure, Long> {
    List<NonTrainingResourceExpenditure> findByNonTrainingResource_ResourceId(Long resourceId);
}

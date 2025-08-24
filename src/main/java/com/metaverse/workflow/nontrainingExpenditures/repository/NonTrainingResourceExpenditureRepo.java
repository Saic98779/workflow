package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingResourceExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonTrainingResourceExpenditureRepo  extends JpaRepository<NonTrainingResourceExpenditure, Long> {
}

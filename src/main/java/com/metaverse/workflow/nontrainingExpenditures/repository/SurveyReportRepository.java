package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.aleap_handholding.SurveyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyReportRepository extends JpaRepository<SurveyReport,Long> {

    List<SurveyReport> findByNonTrainingSubActivity_subActivityId(Long subActivityId);
}

package com.metaverse.workflow.formalisationcompliance.repository;

import com.metaverse.workflow.model.aleap_handholding.FormalisationCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormalisationComplianceRepository extends JpaRepository<FormalisationCompliance, Long> {

    List<FormalisationCompliance> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);

}

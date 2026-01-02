package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.GovtSchemeApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovtSchemeApplicationRepository extends JpaRepository<GovtSchemeApplication,Long> {
    List<GovtSchemeApplication> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

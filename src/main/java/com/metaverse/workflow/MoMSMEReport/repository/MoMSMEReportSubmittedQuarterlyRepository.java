package com.metaverse.workflow.MoMSMEReport.repository;

import com.metaverse.workflow.model.MoMSMEReportSubmittedQuarterly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoMSMEReportSubmittedQuarterlyRepository extends JpaRepository<MoMSMEReportSubmittedQuarterly, Long> {
    Optional<MoMSMEReportSubmittedQuarterly> findByMoMSMEReportSubmitted_SubmittedIdAndQuarter(Long submittedId, String quarter);
}

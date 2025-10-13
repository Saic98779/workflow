package com.metaverse.workflow.MoMSMEReport.repository;

import com.metaverse.workflow.model.MoMSMEReportSubmittedMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MoMSMEReportSubmittedMonthlyRepository extends JpaRepository<MoMSMEReportSubmittedMonthly, Long> {
    Optional<MoMSMEReportSubmittedMonthly> findByMoMSMEReportSubmitted_SubmittedIdAndMonth(Long submittedId, String month);
    List<MoMSMEReportSubmittedMonthly> findByMoMSMEReportSubmitted_SubmittedId(Long submittedId);
}

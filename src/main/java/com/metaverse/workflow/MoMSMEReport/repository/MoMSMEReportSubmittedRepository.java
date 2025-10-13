package com.metaverse.workflow.MoMSMEReport.repository;

import com.metaverse.workflow.model.MoMSMEReportSubmitted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MoMSMEReportSubmittedRepository extends JpaRepository<MoMSMEReportSubmitted, Long> {

    Optional<MoMSMEReportSubmitted> findByFinancialYearAndMoMSMEReport_MoMSMEActivityId(
            String financialYear, Long moMSMEActivityId
    );
}

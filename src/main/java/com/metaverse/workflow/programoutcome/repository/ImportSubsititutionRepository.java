package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ImportSubsititution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ImportSubsititutionRepository extends JpaRepository<ImportSubsititution,Long> {
    List<ImportSubsititution> findByAgencyAgencyId(Long agencyId);
    long countByAgencyAgencyIdAndBusinessPlanSubmissionDateBetween(Long agencyId, Date start, Date end);
    long countByBusinessPlanSubmissionDateBetween(Date start, Date end);

    default long countImportSubsititution(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByBusinessPlanSubmissionDateBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndBusinessPlanSubmissionDateBetween(agencyId, start, end);
        }
    }

}

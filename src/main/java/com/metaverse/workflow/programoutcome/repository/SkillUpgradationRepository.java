package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.SkillUpgradation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SkillUpgradationRepository extends JpaRepository<SkillUpgradation,Long> {
    List<SkillUpgradation> findByAgencyAgencyId(Long agencyId);
    long countByAgencyAgencyIdAndBusinessPlanSubmissionDateBetween(Long agencyId, Date start, Date end);
    long countByBusinessPlanSubmissionDateBetween(Date start, Date end);

    default long countSkillUpgradation(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByBusinessPlanSubmissionDateBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndBusinessPlanSubmissionDateBetween(agencyId, start, end);
        }
    }

    Page<SkillUpgradation> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

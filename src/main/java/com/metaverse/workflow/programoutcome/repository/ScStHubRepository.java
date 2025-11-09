package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ScStHub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ScStHubRepository extends JpaRepository<ScStHub,Long> {
    long countByAgencyAgencyIdAndCreatedOnBetween(Long agencyId, Date start, Date end);
    long countByCreatedOnBetween(Date start, Date end);

    default long countScStHub(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByCreatedOnBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndCreatedOnBetween(agencyId, start, end);
        }
    }

    List<ScStHub> findByAgencyAgencyId(Long agencyId);

    Page<ScStHub> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

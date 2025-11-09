package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.PMViswakarma;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PMViswakarmaReposiroty extends JpaRepository<PMViswakarma,Long> {
    default long countPMViswakarma(Long agencyId, Date start, Date end) {
        return 0;
    }

    long countByDateOfCreditAvailedBetween(Date start, Date end);

    long countByAgencyAgencyIdAndDateOfCreditAvailedBetween(Long agencyId, Date start, Date end);

    List<PMViswakarma> findByAgencyAgencyId(Long agencyId);

    Page<PMViswakarma> findByAgency_AgencyId(Long agencyId, Pageable pageable);
}

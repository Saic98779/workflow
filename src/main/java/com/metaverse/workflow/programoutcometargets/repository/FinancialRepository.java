package com.metaverse.workflow.programoutcometargets.repository;

import com.metaverse.workflow.model.FinancialTarget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialRepository extends JpaRepository<FinancialTarget,Long> {
    List<FinancialTarget> findByAgencyAgencyId(Long agencyId);
}

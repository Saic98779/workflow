package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GreeningOfMSME;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GreeningOfMSMERepository extends JpaRepository<GreeningOfMSME,Long> {
    List<GreeningOfMSME> findByAgencyAgencyId(Long agencyId);
}

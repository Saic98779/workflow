package com.metaverse.workflow.agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Agency;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long>{

    Optional<Agency> findByAgencyName(String agencyName);
}

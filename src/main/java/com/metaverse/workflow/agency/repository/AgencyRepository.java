package com.metaverse.workflow.agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Agency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long>{

    Optional<Agency> findByAgencyName(String agencyName);
    @Query("SELECT a.agencyName FROM Agency a WHERE a.id IN :ids")
    List<String> findAgencyNamesByIds(@Param("ids") List<Long> ids);
}

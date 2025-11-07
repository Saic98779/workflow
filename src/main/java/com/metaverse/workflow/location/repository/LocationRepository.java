package com.metaverse.workflow.location.repository;

import java.util.List;

import com.metaverse.workflow.model.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
	
	public List<Location> findByAgencyAgencyId(Long agencyId);

    Location findByLocationName(String locationName);

    Page<Location> findByAgencyAgencyId(Long agencyId, Pageable pageable);
}

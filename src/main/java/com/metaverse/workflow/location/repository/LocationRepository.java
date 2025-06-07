package com.metaverse.workflow.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
	
	public List<Location> findByAgencyAgencyId(Long agencyId);

    Location findByLocationName(String locationName);
}

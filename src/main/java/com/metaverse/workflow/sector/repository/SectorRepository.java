package com.metaverse.workflow.sector.repository;

import com.metaverse.workflow.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector,Integer> {

    Sector findBySectorName(String sectorName);
}

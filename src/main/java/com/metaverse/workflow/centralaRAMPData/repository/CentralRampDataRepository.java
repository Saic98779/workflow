package com.metaverse.workflow.centralaRAMPData.repository;

import com.metaverse.workflow.centralaRAMPData.encryption.CentralRampData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentralRampDataRepository extends JpaRepository<CentralRampData, Integer> {
}
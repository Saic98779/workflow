package com.metaverse.workflow.ProgramMonitoring.repository;

import com.metaverse.workflow.model.ProgramMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramMonitoringRepo extends JpaRepository<ProgramMonitoring,Long> {
    List<ProgramMonitoring> findByProgramId(Long programId);
    boolean existsByProgramId(Long programId);
}

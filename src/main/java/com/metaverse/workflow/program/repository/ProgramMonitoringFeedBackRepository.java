package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramMonitoringFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramMonitoringFeedBackRepository extends JpaRepository<ProgramMonitoringFeedBack, Long> {
    List<ProgramMonitoringFeedBack> findByProgramId(Long programId);

    boolean existsByProgramId(Long programId);
}

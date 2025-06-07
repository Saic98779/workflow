package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.MediaCoverage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaCoverageRepository  extends JpaRepository<MediaCoverage, Long> {
    void deleteByProgramProgramId(Long programId);
}

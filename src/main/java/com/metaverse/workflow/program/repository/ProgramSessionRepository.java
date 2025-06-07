package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramSessionRepository extends JpaRepository<ProgramSession, Long> {
    void deleteByProgramProgramId(Long programId);

    List<ProgramSession> findByProgram_ProgramId(Long programId);

    Boolean existsByResource_ResourceId(Long resourceId);
}

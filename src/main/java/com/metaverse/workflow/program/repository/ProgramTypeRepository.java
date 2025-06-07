package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramTypeRepository extends JpaRepository<ProgramType,Integer> {
    List<ProgramType> findByAgencyAgencyId(Long id);
}

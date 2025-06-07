package com.metaverse.workflow.programrawmaterial.repository;

import com.metaverse.workflow.model.ProgramAttendanceId;
import com.metaverse.workflow.model.ProgramRawMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRawMaterialRepository extends JpaRepository<ProgramRawMaterial, ProgramAttendanceId> {

    @Query("SELECT p FROM ProgramRawMaterial p WHERE p.programAttendanceId.programId = :programId")
    List<ProgramRawMaterial> findByProgramRawMaterials(Long programId);

}
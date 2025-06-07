package com.metaverse.workflow.programattendance.repository;

import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.model.ProgramAttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramAttendanceRepository extends JpaRepository<ProgramAttendance, ProgramAttendanceId> {
    @Query("SELECT  p FROM ProgramAttendance p WHERE p.programAttendanceId.programId = :programId")
    List<ProgramAttendance> findByProgramAttendances(Long programId);
}
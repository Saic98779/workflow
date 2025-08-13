package com.metaverse.workflow.participant.repository;

import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.ParticipantTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipantTempRepository extends JpaRepository<ParticipantTemp, Long> {
    Page<ParticipantTemp> findByPrograms_Agency_AgencyId(Long agencyId, Pageable pageable);

    @Query("SELECT p FROM ParticipantTemp p JOIN p.programs pr WHERE pr.programId = :programId")
    Page<ParticipantTemp> findByProgramId(Long programId, Pageable pageable);

    List<ParticipantTemp> findByHasErrorTrueAndIsDeletedFalse();

    List<ParticipantTemp> findByIsDeletedFalse();

    List<ParticipantTemp> findByPrograms_Agency_AgencyIdAndIsDeletedFalse(Long agencyId);

    Page<ParticipantTemp> findByPrograms_ProgramIdAndIsDeletedFalse(Long programId, Pageable pageable);

    List<ParticipantTemp> findByPrograms_ProgramIdAndIsDeletedFalse(Long programId);

    @Query("SELECT p.participantTempId FROM ParticipantTemp p JOIN p.programs pr WHERE pr.programId = :programId AND p.isDeleted = false")
    List<Long> findParticipantTempIdsByProgramId(Long programId);

    ParticipantTemp findByParticipantTempId(Long id);
}

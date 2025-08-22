package com.metaverse.workflow.participant.repository;

import com.metaverse.workflow.model.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Participant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByPrograms(Program program);

    List<Participant> findByOrganizationMandal(String mandal);

    Participant findByMobileNo(Long mobileNo);

    @Query("SELECT DISTINCT p FROM Participant p JOIN p.programs prog WHERE prog.programType = :typeOfProgram")
    List<Participant> findByProgramType(@Param("typeOfProgram") String typeOfProgram);

    @Query("SELECT p FROM Participant p JOIN p.programs pr WHERE pr.programId = :programId")
    Page<Participant> findByProgramId(Long programId, Pageable pageable);

    Page<Participant> findByPrograms_ProgramId(Long programId, Pageable pageable);

    List<Participant> findByPrograms_ProgramId(Long programId);

    Page<Participant> findByPrograms_Agency_AgencyId(Long agencyId, Pageable pageable);

    boolean existsByParticipantNameAndOrganization_OrganizationName(String participantName, String s);

    @Query("SELECT DISTINCT p FROM Participant p " +
            "JOIN p.programs pr " +
            "JOIN pr.agency ag " +
            "WHERE ag.agencyId = :agencyId " +
            "AND pr.createdOn BETWEEN :startDate AND :endDate")
    List<Participant> findAllByAgencyIdAndProgramCreatedOnBetween(
            @Param("agencyId") Long agencyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    @Query("SELECT pr.activityId, COUNT(DISTINCT p) " +
            "FROM Participant p " +
            "JOIN p.programs pr " +
            "WHERE pr.agency.agencyId = :agencyId " +
            "GROUP BY pr.activityId")
    List<Object[]> countParticipantsByAgencyGroupedByActivity(@Param("agencyId") Long agencyId);
}

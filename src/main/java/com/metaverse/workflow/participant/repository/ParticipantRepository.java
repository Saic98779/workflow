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

    @Query("SELECT DISTINCT p FROM Participant p JOIN p.programs pr WHERE pr.programId = :programId")
    List<Participant> findByProgramId(@Param("programId") Long programId);

    Page<Participant> findByPrograms_Agency_AgencyId(Long agencyId, Pageable pageable);

    List<Participant> findByPrograms_Agency_AgencyId(Long agencyId);

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

    /**
     * Lightweight projection of {@link #findAllByAgencyIdAndProgramCreatedOnBetween}:
     * one row per (participantId, subActivityId of any of the participant's programs, participant createdOn)
     * for participants having at least one program of the agency created in the date range.
     */
    @Query("SELECT DISTINCT p.participantId, pr.subActivityId, p.createdOn FROM Participant p " +
            "JOIN p.programs pr " +
            "WHERE EXISTS (SELECT 1 FROM Participant p2 JOIN p2.programs pr2 " +
            "              WHERE p2 = p AND pr2.agency.agencyId = :agencyId " +
            "              AND pr2.createdOn BETWEEN :startDate AND :endDate)")
    List<Object[]> findParticipantSubActivityRowsByAgencyIdAndProgramCreatedOnBetween(
            @Param("agencyId") Long agencyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    /** Distinct participants per sub-activity for the agency's programs created in the date range: rows of [subActivityId, count]. */
    @Query("SELECT pr.subActivityId, COUNT(DISTINCT p.participantId) FROM Participant p " +
            "JOIN p.programs pr " +
            "WHERE pr.agency.agencyId = :agencyId " +
            "AND pr.createdOn BETWEEN :startDate AND :endDate " +
            "GROUP BY pr.subActivityId")
    List<Object[]> countParticipantsBySubActivityAndProgramCreatedOnBetween(
            @Param("agencyId") Long agencyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);


    @Query("SELECT pr.activityId, COUNT(DISTINCT p) " +
            "FROM Participant p " +
            "JOIN p.programs pr " +
            "WHERE pr.agency.agencyId = :agencyId " +
            "GROUP BY pr.activityId")
    List<Object[]> countParticipantsByAgencyGroupedByActivity(@Param("agencyId") Long agencyId);

    @Query(
            value = """
            SELECT DISTINCT p.participant_id, p.participant_name
            FROM participant p
            JOIN program_participant pp 
                ON p.participant_id = pp.participant_id
            JOIN program pr 
                ON pp.program_id = pr.program_id
            WHERE pr.agency_id = :agencyId
            """,
            nativeQuery = true
    )
    List<Object[]> findParticipantsByAgencyId(@Param("agencyId") Long agencyId);

    List<Participant> findByOrganization_OrganizationId(Long organizationId);

}

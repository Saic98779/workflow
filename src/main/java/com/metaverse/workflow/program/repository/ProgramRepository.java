package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    Page<Program> findByAgencyAgencyId(Long agencyId, Pageable pageable);

    List<Program> findByAgencyAgencyId(Long agencyId);

    List<Program> findByAgencyAgencyIdAndStatus(Long agencyId, String status);

    List<Program> findByAgencyAgencyIdAndStatusIn(Long agencyId, List<String> statuses);

    Boolean existsByLocation_LocationId(Long locationId);

    @Query("SELECT p FROM Program p WHERE p.startDate <= :targetDate")
    List<Program> findProgramsWithStartDateEqual(@Param("targetDate") Date targetDate);

    List<Program> findByStartDateBefore(Date today);


    List<Program> findByLocation_District(String district);

    List<Program> findByAgency_AgencyIdAndLocation_District(Long agencyId, String district);

    List<Program> findByStartDateBetween(Date startDate, Date endDate);

    List<Program> findByAgency_AgencyIdAndStartDateBetween(Long agencyId, Date startDate, Date endDate);

    List<Program> findAllByOrderByStartDateAsc();

    List<Program> findByAgency_AgencyIdOrderByStartDateAsc(Long id);


    default List<Program> getAllPrograms(Long agencyId) {
        return null;
    }

    List<Program> findByAgency_AgencyIdAndLocation_DistrictAndStatus(Long id, String district, String programExecutionUpdated);

    List<Program> findByAgency_AgencyIdAndStatus(Long id, String participantsAdded);
    default Page<Program> findByAgencyAgencyStatusId(Long agencyId, Pageable pageable, String status) {
        Date today = new Date();
        if(agencyId == -1) {
            return switch (status) {
                case "programsScheduled" -> findProgramScheduled(pageable);
                case "programsInProcess" -> findProgramInProcess(today, pageable);
                case "programsCompleted" -> findProgramsCompleted(today, pageable);
                case "programsCompletedDataPending" -> findProgramsCompletedDataPending( today, pageable);
                case "programsOverdue" -> findProgramOverDue(today, pageable);
                case "programsYetToBegin" -> findProgramYetToBegin(today, pageable);
                default -> Page.empty(pageable);
            };
        }
        return switch (status) {
            case "programsScheduled" -> findProgramScheduled(agencyId, today, pageable);
            case "programsInProcess" -> findProgramInProcess(agencyId, today, pageable);
            case "programsCompleted" -> findProgramsCompleted(agencyId, today, pageable);
            case "programsCompletedDataPending" -> findProgramsCompletedDataPending(agencyId, today, pageable);
            case "programsOverdue" -> findProgramOverDue(agencyId, today, pageable);
            case "programsYetToBegin" -> findProgramYetToBegin(agencyId, today, pageable);
            default -> Page.empty(pageable);
        };
    }

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId ")
    Page<Program> findProgramScheduled(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId AND p.startDate <= :today AND p.endDate >= :today")
    Page<Program> findProgramInProcess(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId AND p.endDate < :today AND LOWER(p.status) = LOWER('Program Expenditure Updated')")
    Page<Program> findProgramsCompleted(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId AND p.endDate < :today AND p.status <> 'Program Expenditure Updated' AND p.participants IS NOT EMPTY")
    Page<Program> findProgramsCompletedDataPending(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId AND p.endDate < :today AND p.status <> 'Program Expenditure Updated' AND p.participants IS EMPTY")
    Page<Program> findProgramOverDue(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.agency.agencyId = :agencyId AND p.startDate > :today")
    Page<Program> findProgramYetToBegin(@Param("agencyId") Long agencyId, @Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p")
    Page<Program> findProgramScheduled(Pageable pageable);

    @Query("SELECT p FROM Program p WHERE  p.startDate <= :today AND p.endDate >= :today")
    Page<Program> findProgramInProcess(@Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.endDate < :today AND LOWER(p.status) = LOWER('Program Expenditure Updated')")
    Page<Program> findProgramsCompleted(@Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE  p.endDate < :today AND p.status <> 'Program Expenditure Updated' AND p.participants IS NOT EMPTY")
    Page<Program> findProgramsCompletedDataPending(@Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE p.endDate < :today AND p.status <> 'Program Expenditure Updated' AND p.participants IS EMPTY")
    Page<Program> findProgramOverDue(@Param("today") Date today, Pageable pageable);

    @Query("SELECT p FROM Program p WHERE  p.startDate > :today")
    Page<Program> findProgramYetToBegin(@Param("today") Date today, Pageable pageable);

    List<Program> findByStartDateBetweenOrderByStartDateAsc(Date startDate, Date endDate);

    @Query("SELECT p FROM Program p " +
            "WHERE LOWER(p.location.district) = LOWER(:district) " +
            "AND p.endDate >= CURRENT_DATE")
    Page<Program> getProgramsByDistrict(@Param("district") String district,
                                        Pageable pageable);

    @Query("SELECT pr.activityId, COUNT(DISTINCT pr) " +
            "FROM Program pr " +
            "JOIN pr.participants p " +      // only programs with at least 1 participant
            "WHERE pr.agency.agencyId = :agencyId " +
            "GROUP BY pr.activityId")
    List<Object[]> countProgramsWithParticipantsByActivity(@Param("agencyId") Long agencyId);

}

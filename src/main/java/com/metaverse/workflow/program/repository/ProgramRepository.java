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
}

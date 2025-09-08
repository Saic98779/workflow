package com.metaverse.workflow.activity.repository;

import com.metaverse.workflow.ProgramMonitoring.service.SubActivityParticipantCountDTO;
import com.metaverse.workflow.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository  extends JpaRepository<Activity,Long> {

    List<Activity> findByAgencyAgencyId(Long id);

    Optional<Activity> findByActivityName(String activityName);

    Activity findByActivityId(Long activityId);

    @Query("SELECT new com.metaverse.workflow.ProgramMonitoring.service.SubActivityParticipantCountDTO( " +
            "pr.subActivityId, COUNT(DISTINCT pr.programId)) " +
            "FROM Program pr " +
            "JOIN pr.participants p " +
            "WHERE pr.agency.agencyId = :agencyId " +
            "GROUP BY pr.subActivityId")
    List<SubActivityParticipantCountDTO> findProgramCountByAgencyId(@Param("agencyId") Long agencyId);

}

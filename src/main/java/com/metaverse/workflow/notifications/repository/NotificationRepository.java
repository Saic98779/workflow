package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.enums.NotificationStatus;
import com.metaverse.workflow.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {


    @Query("""
    SELECT DISTINCT n
    FROM Notifications n
    JOIN n.remarksByCallCenter r
    WHERE n.callCenterAgent.userId = :userId
      AND n.status IN :statuses
    ORDER BY r.remarkedAt DESC
""")
    List<Notifications> findByCallCenterAgent_UserIdAndStatusInOrderByRemarkedAtDesc(
            @Param("userId") String userId,
            @Param("statuses") List<NotificationStatus> statuses
    );


    @Query("""
    SELECT DISTINCT n
    FROM Notifications n
    JOIN n.remarksByAgency r
    WHERE n.agency.agencyId = :agencyId
      AND n.status IN :statuses
    ORDER BY r.remarkedAt DESC
""")
    List<Notifications> findByAgency_AgencyIdAndStatusInOrderByRemarkedAtDesc(
            @Param("agencyId") Long agencyId,
            @Param("statuses") List<NotificationStatus> statuses
    );


    List<Notifications> findByAgency_AgencyId(Long agencyId);


    List<Notifications> findByCallCenterAgent_UserId(String userId);
}

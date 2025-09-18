package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.enums.NotificationStatus;
import com.metaverse.workflow.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {


    List<Notifications> findByCallCenterAgent_UserIdAndStatusIn(
            String userId,
            List<NotificationStatus> statuses
    );
    List<Notifications> findByAgency_AgencyIdAndStatusIn(Long agencyId, List<NotificationStatus> statuses);

    List<Notifications> findByAgency_AgencyId(Long agencyId);


    List<Notifications> findByCallCenterAgent_UserId(String userId);
}

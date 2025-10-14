package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.model.GlobalNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GlobalNotificationRepository extends JpaRepository<GlobalNotifications,Long> {
    List<GlobalNotifications> findByRecipientUser_UserId(String userId);
    List<GlobalNotifications> findByRecipientType(NotificationRecipientType recipientType);

    Collection<GlobalNotifications> findByRecipientTypeAndAgency_AgencyId(NotificationRecipientType type, Long agencyId);
}

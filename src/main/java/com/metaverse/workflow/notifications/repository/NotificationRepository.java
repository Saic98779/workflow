package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import com.metaverse.workflow.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {


    List<Notifications> findByRecipientTypeAndIsRead(NotificationRecipientType role,Boolean isRead);

    Collection<Notifications> findByReceiver_UserIdOrderByLastMessageAtDesc(String userId);

    List<Notifications> findByIsReadAndAgency_agencyId(Boolean isRead,Long agencyId);
}

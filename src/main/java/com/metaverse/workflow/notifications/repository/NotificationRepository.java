package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByProgramProgramId(Long programId);
}

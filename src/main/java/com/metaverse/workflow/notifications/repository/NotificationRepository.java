package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserId(String userId);

//    void deleteByProgramProgramId(Long programId);
}

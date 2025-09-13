package com.metaverse.workflow.notifications.repository;

import com.metaverse.workflow.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {

    Optional<Notifications> findByApplicationNo(String applicationNo);

//    void deleteByProgramProgramId(Long programId);
}

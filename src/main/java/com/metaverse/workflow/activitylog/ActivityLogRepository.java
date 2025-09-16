package com.metaverse.workflow.activitylog;

import com.metaverse.workflow.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}

package com.metaverse.workflow.common.logs;

import com.metaverse.workflow.model.ActivityLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<ActivityLogs,Long> {
}

package com.metaverse.workflow.activitylog;

import com.metaverse.workflow.model.ActivityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    @Async("taskExecutor")
    public void logs(String username, String action, String description, String module,String url) {
        ActivityLog log = ActivityLog.builder()
                .username(username)
                .action(action)
                .description(description)
                .module(module)
                .apiUrl(url)
                .timestamp(LocalDateTime.now())
                .build();
        activityLogRepository.save(log);

    }


}
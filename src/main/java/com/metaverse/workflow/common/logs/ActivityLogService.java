package com.metaverse.workflow.common.logs;

import com.metaverse.workflow.model.ActivityLogs;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service@RequiredArgsConstructor
public class ActivityLogService {

    private final LogRepository logRepository;

    @Async("taskExecutor")
    public void logs(/*String username,*/ String action, String module, String discretion) {
        System.out.println(Thread.currentThread()+" log");
        ActivityLogs log = ActivityLogs.builder()
                .username("username")
                .action(action)
                .module(module)
                .discretion(discretion)
                .timestamp(LocalDateTime.now())
                .build();
        logRepository.save(log);

    }

}

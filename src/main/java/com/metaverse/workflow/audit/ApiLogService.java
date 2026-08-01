package com.metaverse.workflow.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ApiLogService {

    @Autowired
    private ApiLogRepository apiLogRepository;

    public ApiLog save(ApiLog apiLog) {
        return apiLogRepository.save(apiLog);
    }

    @Async
    public void saveAsync(ApiLog apiLog) {
        apiLogRepository.save(apiLog);
    }
}


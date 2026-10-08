package com.metaverse.workflow.audit.bulk.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BulkAopLogService {

    @Autowired
    private BulkAopLogRepository bulkAopLogRepository;

    public BulkAopLog save(BulkAopLog bulkAopLog) {
        return bulkAopLogRepository.save(bulkAopLog);
    }

    @Async
    public void saveAsync(BulkAopLog bulkAopLog) {
        bulkAopLogRepository.save(bulkAopLog);
    }
}

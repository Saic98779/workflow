package com.metaverse.workflow.audit.bulk.aop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface BulkAopLogRepository extends JpaRepository<BulkAopLog, Long> {

    Page<BulkAopLog> findAllByOrderByTimestampDesc(Pageable pageable);

    Page<BulkAopLog> findByTableNameOrderByTimestampDesc(String tableName, Pageable pageable);

    Page<BulkAopLog> findByUsernameOrderByTimestampDesc(String username, Pageable pageable);

    Page<BulkAopLog> findByTimestampBetweenOrderByTimestampAsc(Instant from, Instant to, Pageable pageable);
}

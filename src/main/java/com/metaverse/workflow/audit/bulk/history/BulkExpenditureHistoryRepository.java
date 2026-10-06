package com.metaverse.workflow.audit.bulk.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface BulkExpenditureHistoryRepository extends JpaRepository<BulkExpenditureHistory, Long> {

    Page<BulkExpenditureHistory> findAllByOrderByChangedAtDesc(Pageable pageable);

    Page<BulkExpenditureHistory> findByRecordIdOrderByChangedAtDesc(Long recordId, Pageable pageable);

    Page<BulkExpenditureHistory> findByActionOrderByChangedAtDesc(String action, Pageable pageable);

    Page<BulkExpenditureHistory> findByChangedByOrderByChangedAtDesc(String changedBy, Pageable pageable);

    Page<BulkExpenditureHistory> findByChangedAtBetweenOrderByChangedAtAsc(Instant from, Instant to, Pageable pageable);

    Page<BulkExpenditureHistory> findByCorrelationIdOrderByChangedAtAsc(String correlationId, Pageable pageable);
}

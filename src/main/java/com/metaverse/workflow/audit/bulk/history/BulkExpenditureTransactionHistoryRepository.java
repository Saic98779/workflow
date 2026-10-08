package com.metaverse.workflow.audit.bulk.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface BulkExpenditureTransactionHistoryRepository
        extends JpaRepository<BulkExpenditureTransactionHistory, Long> {

    Page<BulkExpenditureTransactionHistory> findAllByOrderByChangedAtDesc(Pageable pageable);

    Page<BulkExpenditureTransactionHistory> findByRecordIdOrderByChangedAtDesc(Long recordId, Pageable pageable);

    Page<BulkExpenditureTransactionHistory> findByActionOrderByChangedAtDesc(String action, Pageable pageable);

    Page<BulkExpenditureTransactionHistory> findByChangedByOrderByChangedAtDesc(String changedBy, Pageable pageable);

    Page<BulkExpenditureTransactionHistory> findByChangedAtBetweenOrderByChangedAtAsc(Instant from,
                                                                                     Instant to,
                                                                                     Pageable pageable);

    Page<BulkExpenditureTransactionHistory> findByCorrelationIdOrderByChangedAtAsc(String correlationId,
                                                                                    Pageable pageable);
}

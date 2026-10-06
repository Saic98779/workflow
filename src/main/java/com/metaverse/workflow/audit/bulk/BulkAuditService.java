package com.metaverse.workflow.audit.bulk;

import com.metaverse.workflow.audit.bulk.counter.BulkTableCounter;
import com.metaverse.workflow.audit.bulk.counter.BulkTableCounterRepository;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureHistoryRepository;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureTransactionHistoryRepository;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureRepository;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Read helpers over the separately tracked bulk tables: the per-table counters plus a way to
 * resynchronise the tracked row count with the real tables.
 */
@Service
public class BulkAuditService {

    private final BulkTableCounterRepository counterRepository;
    private final BulkExpenditureHistoryRepository expenditureHistoryRepository;
    private final BulkExpenditureTransactionHistoryRepository transactionHistoryRepository;
    private final BulkExpenditureRepository expenditureRepository;
    private final BulkExpenditureTransactionRepository transactionRepository;

    public BulkAuditService(BulkTableCounterRepository counterRepository,
                            BulkExpenditureHistoryRepository expenditureHistoryRepository,
                            BulkExpenditureTransactionHistoryRepository transactionHistoryRepository,
                            BulkExpenditureRepository expenditureRepository,
                            BulkExpenditureTransactionRepository transactionRepository) {
        this.counterRepository = counterRepository;
        this.expenditureHistoryRepository = expenditureHistoryRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.expenditureRepository = expenditureRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<BulkTableCounter> getCounters() {
        return counterRepository.findAllByOrderByTableNameAsc();
    }

    @Transactional(readOnly = true)
    public long getExpenditureHistoryCount() {
        return expenditureHistoryRepository.count();
    }

    @Transactional(readOnly = true)
    public long getTransactionHistoryCount() {
        return transactionHistoryRepository.count();
    }

    /**
     * Recomputes the tracked row count from the real tables. Useful after bulk JPQL operations
     * (derived delete/update queries) that bypass Hibernate entity events.
     */
    @Transactional
    public List<BulkTableCounter> resynchroniseCounters() {
        setRowCount(BulkTable.BULK_EXPENDITURE, expenditureRepository.count());
        setRowCount(BulkTable.BULK_EXPENDITURE_TRANSACTION, transactionRepository.count());
        return counterRepository.findAllByOrderByTableNameAsc();
    }

    private void setRowCount(BulkTable table, long rowCount) {
        counterRepository.findByTableName(table.getTableName()).ifPresent(counter -> {
            counter.setRowCount(rowCount);
            counter.setUpdatedAt(Instant.now());
            counterRepository.save(counter);
        });
    }
}

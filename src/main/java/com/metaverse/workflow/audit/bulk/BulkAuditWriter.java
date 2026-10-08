package com.metaverse.workflow.audit.bulk;

import com.metaverse.workflow.audit.bulk.counter.BulkTableCounter;
import com.metaverse.workflow.audit.bulk.counter.BulkTableCounterRepository;
import com.metaverse.workflow.audit.bulk.history.BulkChangeHistory;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureHistory;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureHistoryRepository;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureTransactionHistory;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureTransactionHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Persists the captured row changes into the per-table history tables and bumps the per-table counters.
 * Always runs in its own transaction so it is completely decoupled from the business transaction
 * that produced the changes.
 */
@Service
public class BulkAuditWriter {

    private static final Logger log = LoggerFactory.getLogger(BulkAuditWriter.class);

    private final BulkExpenditureHistoryRepository expenditureHistoryRepository;
    private final BulkExpenditureTransactionHistoryRepository transactionHistoryRepository;
    private final BulkTableCounterRepository counterRepository;

    public BulkAuditWriter(BulkExpenditureHistoryRepository expenditureHistoryRepository,
                           BulkExpenditureTransactionHistoryRepository transactionHistoryRepository,
                           BulkTableCounterRepository counterRepository) {
        this.expenditureHistoryRepository = expenditureHistoryRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.counterRepository = counterRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(List<BulkChangeRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        Instant now = Instant.now();

        // Counters first: the bulk update query clears the persistence context, so nothing
        // should still be pending afterwards.
        Map<BulkTable, long[]> deltas = new EnumMap<>(BulkTable.class);
        Map<BulkTable, BulkChangeAction> lastActions = new EnumMap<>(BulkTable.class);
        for (BulkChangeRecord record : records) {
            long[] delta = deltas.computeIfAbsent(record.table(), key -> new long[3]);
            if (record.action() == BulkChangeAction.INSERT) {
                delta[0]++;
            } else if (record.action() == BulkChangeAction.UPDATE) {
                delta[1]++;
            } else {
                delta[2]++;
            }
            lastActions.put(record.table(), record.action());
        }
        for (Map.Entry<BulkTable, long[]> entry : deltas.entrySet()) {
            long[] delta = entry.getValue();
            bumpCounter(entry.getKey(), delta[0], delta[1], delta[2], lastActions.get(entry.getKey()), now);
        }

        List<BulkExpenditureHistory> expenditureRows = new ArrayList<>();
        List<BulkExpenditureTransactionHistory> transactionRows = new ArrayList<>();
        for (BulkChangeRecord record : records) {
            if (record.table() == BulkTable.BULK_EXPENDITURE) {
                BulkExpenditureHistory row = new BulkExpenditureHistory();
                apply(record, row, now);
                expenditureRows.add(row);
            } else {
                BulkExpenditureTransactionHistory row = new BulkExpenditureTransactionHistory();
                apply(record, row, now);
                transactionRows.add(row);
            }
        }
        if (!expenditureRows.isEmpty()) {
            expenditureHistoryRepository.saveAll(expenditureRows);
        }
        if (!transactionRows.isEmpty()) {
            transactionHistoryRepository.saveAll(transactionRows);
        }
    }

    private void bumpCounter(BulkTable table,
                             long inserts,
                             long updates,
                             long deletes,
                             BulkChangeAction action,
                             Instant at) {
        try {
            ensureCounterRowExists(table, at);
            counterRepository.bumpCounters(
                    table.getTableName(),
                    inserts,
                    updates,
                    deletes,
                    action == null ? null : action.name(),
                    at
            );
        } catch (Exception e) {
            log.warn("Failed to update counters for table {}", table.getTableName(), e);
        }
    }

    private void ensureCounterRowExists(BulkTable table, Instant at) {
        if (counterRepository.findByTableName(table.getTableName()).isPresent()) {
            return;
        }
        try {
            BulkTableCounter counter = new BulkTableCounter();
            counter.setTableName(table.getTableName());
            counter.setInsertCount(0L);
            counter.setUpdateCount(0L);
            counter.setDeleteCount(0L);
            counter.setRowCount(0L);
            counter.setUpdatedAt(at);
            counterRepository.saveAndFlush(counter);
        } catch (DataIntegrityViolationException e) {
            log.debug("Counter row for {} was created concurrently", table.getTableName());
        }
    }

    private void apply(BulkChangeRecord record, BulkChangeHistory row, Instant now) {
        row.setSourceTable(record.table().getTableName());
        row.setRecordId(record.recordId());
        row.setAction(record.action().name());
        row.setChangedBy(record.changedBy());
        row.setCorrelationId(record.correlationId());
        row.setSourceMethod(record.sourceMethod());
        row.setChangedFields(record.changedFields());
        row.setOldState(record.oldState());
        row.setNewState(record.newState());
        row.setChangedAt(now);
    }
}

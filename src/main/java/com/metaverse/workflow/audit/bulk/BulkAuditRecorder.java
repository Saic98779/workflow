package com.metaverse.workflow.audit.bulk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Receives captured row changes and hands them to {@link BulkAuditWriter} once the surrounding
 * transaction commits, so history rows are never written for changes that were rolled back.
 *
 * <p>Nothing in this class participates in the caller's transaction, and every failure is swallowed:
 * tracking must never break the business operation it observes.
 */
@Component
public class BulkAuditRecorder {

    private static final Logger log = LoggerFactory.getLogger(BulkAuditRecorder.class);

    private final ThreadLocal<List<BulkChangeRecord>> pending = new ThreadLocal<>();
    private final ThreadLocal<Boolean> flushing = new ThreadLocal<>();

    private final ObjectProvider<BulkAuditWriter> writerProvider;

    public BulkAuditRecorder(ObjectProvider<BulkAuditWriter> writerProvider) {
        this.writerProvider = writerProvider;
    }

    /**
     * Buffers one captured row change. Callers are the Hibernate listeners only.
     */
    public void enqueue(BulkChangeRecord record) {
        if (record == null || record.table() == null) {
            return;
        }
        if (Boolean.TRUE.equals(flushing.get())) {
            return;
        }
        try {
            List<BulkChangeRecord> buffer = pending.get();
            if (buffer == null) {
                buffer = new ArrayList<>();
                pending.set(buffer);
                deferUntilCommit();
            }
            buffer.add(record);
        } catch (Exception e) {
            log.warn("Failed to buffer bulk audit record for table {}", record.table().getTableName(), e);
        }
    }

    private void deferUntilCommit() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            flushNow();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                flushNow();
            }

            @Override
            public void afterCompletion(int status) {
                pending.remove();
            }
        });
    }

    private void flushNow() {
        List<BulkChangeRecord> batch = pending.get();
        if (batch == null || batch.isEmpty()) {
            return;
        }
        pending.remove();
        flushing.set(Boolean.TRUE);
        try {
            writerProvider.getObject().persist(batch);
        } catch (Exception e) {
            log.warn("Failed to persist bulk audit history for {} record(s)", batch.size(), e);
        } finally {
            flushing.remove();
        }
    }

    /**
     * Flushes anything still buffered on this thread. Used for writes that happen outside a
     * Spring-managed transaction, and as a safety net on shutdown.
     */
    public void flush() {
        flushNow();
    }
}

package com.metaverse.workflow.audit.bulk.history;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;

import java.time.Instant;

/**
 * Shared columns for the per-table change history. Each tracked table gets its own concrete
 * subclass so the history lands in its own table rather than a single shared one.
 */
@MappedSuperclass
public abstract class BulkChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "source_table", length = 64)
    private String sourceTable;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "action", length = 16)
    private String action;

    @Column(name = "changed_by", length = 128)
    private String changedBy;

    @Column(name = "correlation_id", length = 64)
    private String correlationId;

    @Column(name = "source_method", length = 256)
    private String sourceMethod;

    @Column(name = "changed_fields", length = 2048)
    private String changedFields;

    @Lob
    @Column(name = "old_state", columnDefinition = "LONGTEXT")
    private String oldState;

    @Lob
    @Column(name = "new_state", columnDefinition = "LONGTEXT")
    private String newState;

    @Column(name = "changed_at")
    private Instant changedAt;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(String changedFields) {
        this.changedFields = changedFields;
    }

    public String getOldState() {
        return oldState;
    }

    public void setOldState(String oldState) {
        this.oldState = oldState;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}

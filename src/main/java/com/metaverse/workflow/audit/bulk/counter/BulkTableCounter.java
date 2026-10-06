package com.metaverse.workflow.audit.bulk.counter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Per-table counters for the separately tracked bulk tables. One row per tracked table,
 * maintained with atomic increments so concurrent writers do not lose counts.
 */
@Entity
@Table(
        name = "bulk_table_counters",
        indexes = {
                @Index(name = "idx_bulk_counter_table", columnList = "table_name")
        }
)
public class BulkTableCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counter_id")
    private Long counterId;

    @Column(name = "table_name", length = 64, unique = true, nullable = false)
    private String tableName;

    @Column(name = "insert_count", nullable = false)
    private Long insertCount = 0L;

    @Column(name = "update_count", nullable = false)
    private Long updateCount = 0L;

    @Column(name = "delete_count", nullable = false)
    private Long deleteCount = 0L;

    /**
     * Running row count tracked from insert/delete events. Can be resynchronised from the real table
     * with {@code POST /bulk/audit/counters/resync}.
     */
    @Column(name = "row_count", nullable = false)
    private Long rowCount = 0L;

    @Column(name = "last_action", length = 16)
    private String lastAction;

    @Column(name = "last_action_at")
    private Instant lastActionAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getInsertCount() {
        return insertCount;
    }

    public void setInsertCount(Long insertCount) {
        this.insertCount = insertCount;
    }

    public Long getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Long updateCount) {
        this.updateCount = updateCount;
    }

    public Long getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(Long deleteCount) {
        this.deleteCount = deleteCount;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public Instant getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(Instant lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

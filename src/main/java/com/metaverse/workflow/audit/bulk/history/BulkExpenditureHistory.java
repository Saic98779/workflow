package com.metaverse.workflow.audit.bulk.history;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Change history for rows of the {@code bulk_expenditure} table.
 * Populated automatically by the bulk audit listeners; never written by application code.
 */
@Entity
@Table(
        name = "bulk_expenditure_history",
        indexes = {
                @Index(name = "idx_bulk_exp_hist_record", columnList = "record_id"),
                @Index(name = "idx_bulk_exp_hist_changed_at", columnList = "changed_at"),
                @Index(name = "idx_bulk_exp_hist_changed_by", columnList = "changed_by")
        }
)
public class BulkExpenditureHistory extends BulkChangeHistory {
}

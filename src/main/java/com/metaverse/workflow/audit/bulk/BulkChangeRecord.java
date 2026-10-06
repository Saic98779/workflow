package com.metaverse.workflow.audit.bulk;

/**
 * An immutable, already-serialised capture of one row change, ready to be persisted.
 */
public record BulkChangeRecord(
        BulkTable table,
        BulkChangeAction action,
        Long recordId,
        String changedBy,
        String correlationId,
        String sourceMethod,
        String changedFields,
        String oldState,
        String newState
) {
}

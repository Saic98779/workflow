package com.metaverse.workflow.audit.bulk;

/**
 * Holds the collaborators that Hibernate needs when it builds its event listeners.
 *
 * <p>Hibernate instantiates {@link com.metaverse.workflow.audit.bulk.listener.BulkAuditIntegratorProvider}
 * reflectively with a no-arg constructor while the EntityManagerFactory is being created, which is too
 * early for Spring injection. The Spring configuration publishes the beans here instead, and the
 * integrator reads them back.
 */
public final class BulkAuditBridge {

    private static volatile BulkAuditRecorder recorder;
    private static volatile BulkSnapshotSerializer serializer;

    private BulkAuditBridge() {
    }

    public static void publish(BulkAuditRecorder auditRecorder, BulkSnapshotSerializer snapshotSerializer) {
        recorder = auditRecorder;
        serializer = snapshotSerializer;
    }

    public static BulkAuditRecorder recorder() {
        return recorder;
    }

    public static BulkSnapshotSerializer serializer() {
        return serializer;
    }

    public static boolean isReady() {
        return recorder != null && serializer != null;
    }
}

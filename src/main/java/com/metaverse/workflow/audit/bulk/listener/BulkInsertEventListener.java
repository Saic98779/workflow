package com.metaverse.workflow.audit.bulk.listener;

import com.metaverse.workflow.audit.bulk.BulkAuditContext;
import com.metaverse.workflow.audit.bulk.BulkAuditRecorder;
import com.metaverse.workflow.audit.bulk.BulkChangeAction;
import com.metaverse.workflow.audit.bulk.BulkChangeRecord;
import com.metaverse.workflow.audit.bulk.BulkSnapshotSerializer;
import com.metaverse.workflow.audit.bulk.BulkTable;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captures INSERTs on the tracked tables. Registered by {@link BulkAuditIntegrator}.
 */
public class BulkInsertEventListener implements PostInsertEventListener {

    private static final Logger log = LoggerFactory.getLogger(BulkInsertEventListener.class);

    private final BulkAuditRecorder recorder;
    private final BulkSnapshotSerializer serializer;

    public BulkInsertEventListener(BulkAuditRecorder recorder, BulkSnapshotSerializer serializer) {
        this.recorder = recorder;
        this.serializer = serializer;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        try {
            BulkTable table = BulkTable.fromEntityName(event.getPersister().getEntityName());
            if (table == null) {
                return;
            }
            recorder.enqueue(new BulkChangeRecord(
                    table,
                    BulkChangeAction.INSERT,
                    toLong(event.getId()),
                    BulkAuditContext.resolveUsername(null),
                    BulkAuditContext.correlationId(),
                    BulkAuditContext.sourceMethod(),
                    null,
                    null,
                    serializer.toJson(event.getPersister(), event.getState(), event.getSession())
            ));
        } catch (Exception e) {
            log.warn("Bulk audit insert capture failed", e);
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }

    private Long toLong(Object id) {
        return id instanceof Number number ? number.longValue() : null;
    }
}

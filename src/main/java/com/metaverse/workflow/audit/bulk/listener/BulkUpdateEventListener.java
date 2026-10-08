package com.metaverse.workflow.audit.bulk.listener;

import com.metaverse.workflow.audit.bulk.BulkAuditContext;
import com.metaverse.workflow.audit.bulk.BulkAuditRecorder;
import com.metaverse.workflow.audit.bulk.BulkChangeAction;
import com.metaverse.workflow.audit.bulk.BulkChangeRecord;
import com.metaverse.workflow.audit.bulk.BulkSnapshotSerializer;
import com.metaverse.workflow.audit.bulk.BulkTable;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captures UPDATEs on the tracked tables with both the before and after images.
 * Hibernate only raises this event when at least one column is actually dirty.
 * Registered by {@link BulkAuditIntegrator}.
 */
public class BulkUpdateEventListener implements PostUpdateEventListener {

    private static final Logger log = LoggerFactory.getLogger(BulkUpdateEventListener.class);

    private final BulkAuditRecorder recorder;
    private final BulkSnapshotSerializer serializer;

    public BulkUpdateEventListener(BulkAuditRecorder recorder, BulkSnapshotSerializer serializer) {
        this.recorder = recorder;
        this.serializer = serializer;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        try {
            BulkTable table = BulkTable.fromEntityName(event.getPersister().getEntityName());
            if (table == null) {
                return;
            }
            Object id = event.getId();
            recorder.enqueue(new BulkChangeRecord(
                    table,
                    BulkChangeAction.UPDATE,
                    id instanceof Number number ? number.longValue() : null,
                    BulkAuditContext.resolveUsername(null),
                    BulkAuditContext.correlationId(),
                    BulkAuditContext.sourceMethod(),
                    serializer.describeDirtyProperties(event.getPersister(), event.getDirtyProperties()),
                    serializer.toJson(event.getPersister(), event.getOldState(), event.getSession()),
                    serializer.toJson(event.getPersister(), event.getState(), event.getSession())
            ));
        } catch (Exception e) {
            log.warn("Bulk audit update capture failed", e);
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}

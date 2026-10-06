package com.metaverse.workflow.audit.bulk.listener;

import com.metaverse.workflow.audit.bulk.BulkAuditContext;
import com.metaverse.workflow.audit.bulk.BulkAuditRecorder;
import com.metaverse.workflow.audit.bulk.BulkChangeAction;
import com.metaverse.workflow.audit.bulk.BulkChangeRecord;
import com.metaverse.workflow.audit.bulk.BulkSnapshotSerializer;
import com.metaverse.workflow.audit.bulk.BulkTable;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captures DELETEs on the tracked tables. Registered by {@link BulkAuditIntegrator}.
 *
 * <p>Note: derived bulk queries such as {@code deleteByExpenditureBulkExpenditureId} are executed as a
 * single JPQL statement and bypass entity events, so those rows are not represented here.
 */
public class BulkDeleteEventListener implements PostDeleteEventListener {

    private static final Logger log = LoggerFactory.getLogger(BulkDeleteEventListener.class);

    private final BulkAuditRecorder recorder;
    private final BulkSnapshotSerializer serializer;

    public BulkDeleteEventListener(BulkAuditRecorder recorder, BulkSnapshotSerializer serializer) {
        this.recorder = recorder;
        this.serializer = serializer;
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        try {
            BulkTable table = BulkTable.fromEntityName(event.getPersister().getEntityName());
            if (table == null) {
                return;
            }
            Object id = event.getId();
            recorder.enqueue(new BulkChangeRecord(
                    table,
                    BulkChangeAction.DELETE,
                    id instanceof Number number ? number.longValue() : null,
                    BulkAuditContext.resolveUsername(null),
                    BulkAuditContext.correlationId(),
                    BulkAuditContext.sourceMethod(),
                    null,
                    serializer.toJson(event.getPersister(), event.getDeletedState(), event.getSession()),
                    null
            ));
        } catch (Exception e) {
            log.warn("Bulk audit delete capture failed", e);
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}

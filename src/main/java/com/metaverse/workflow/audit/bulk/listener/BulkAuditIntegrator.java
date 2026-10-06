package com.metaverse.workflow.audit.bulk.listener;

import com.metaverse.workflow.audit.bulk.BulkAuditBridge;
import com.metaverse.workflow.audit.bulk.BulkAuditRecorder;
import com.metaverse.workflow.audit.bulk.BulkSnapshotSerializer;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventEngine;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Registers the bulk tracking listeners on the Hibernate event engine.
 *
 * <p>This is the mechanism that lets {@code bulk_expenditure} and {@code bulk_expenditure_transaction}
 * be observed without annotating or otherwise changing the entities themselves.
 */
public class BulkAuditIntegrator implements Integrator {

    private static final Logger log = LoggerFactory.getLogger(BulkAuditIntegrator.class);

    @Override
    public void integrate(Metadata metadata,
                          BootstrapContext bootstrapContext,
                          SessionFactoryImplementor sessionFactory) {
        if (!BulkAuditBridge.isReady()) {
            log.warn("Bulk audit collaborators were not published before the SessionFactory was built; "
                    + "separate tracking for the bulk tables is disabled");
            return;
        }
        BulkAuditRecorder recorder = BulkAuditBridge.recorder();
        BulkSnapshotSerializer serializer = BulkAuditBridge.serializer();

        EventEngine eventEngine = eventEngine(sessionFactory);
        if (eventEngine == null) {
            log.warn("Hibernate EventEngine is not available; separate tracking for the bulk tables is disabled");
            return;
        }
        EventListenerRegistry registry = eventEngine.getListenerRegistry();
        registry.appendListeners(EventType.POST_INSERT, new BulkInsertEventListener(recorder, serializer));
        registry.appendListeners(EventType.POST_UPDATE, new BulkUpdateEventListener(recorder, serializer));
        registry.appendListeners(EventType.POST_DELETE, new BulkDeleteEventListener(recorder, serializer));

        log.info("Bulk audit tracking registered for tables {}",
                List.of("bulk_expenditure", "bulk_expenditure_transaction"));
    }

    private EventEngine eventEngine(SessionFactoryImplementor sessionFactory) {
        try {
            return sessionFactory.getEventEngine();
        } catch (Exception e) {
            log.warn("Could not resolve the Hibernate EventEngine", e);
            return null;
        }
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory,
                             SessionFactoryServiceRegistry serviceRegistry) {
        // listeners are owned by the SessionFactory, nothing to release
    }
}

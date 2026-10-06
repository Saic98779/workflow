package com.metaverse.workflow.audit.bulk.config;

import com.metaverse.workflow.audit.bulk.BulkAuditBridge;
import com.metaverse.workflow.audit.bulk.BulkAuditRecorder;
import com.metaverse.workflow.audit.bulk.BulkSnapshotSerializer;
import com.metaverse.workflow.audit.bulk.listener.BulkAuditIntegratorProvider;
import org.hibernate.jpa.boot.spi.JpaSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the separate bulk table tracking into the application.
 *
 * <p>Both beans below are resolved before the EntityManagerFactory is created: the customizer
 * contributes the integrator provider, and the recorder/serializer are published on
 * {@link BulkAuditBridge} for the integrator to pick up. Nothing else in the application is touched.
 */
@Configuration
public class BulkAuditConfig {

    /**
     * Publishes the collaborators Hibernate needs. Declared as a dependency of the customizer so it is
     * created while the EntityManagerFactory is still being prepared.
     */
    @Bean
    public BulkAuditBridgePublisher bulkAuditBridgePublisher(BulkAuditRecorder recorder,
                                                             BulkSnapshotSerializer serializer) {
        BulkAuditBridge.publish(recorder, serializer);
        return new BulkAuditBridgePublisher();
    }

    /**
     * Registers {@link BulkAuditIntegratorProvider} so the post-insert/update/delete listeners are
     * appended to the Hibernate event engine at bootstrap.
     */
    @Bean
    public HibernatePropertiesCustomizer bulkAuditHibernatePropertiesCustomizer(BulkAuditBridgePublisher publisher) {
        return properties -> properties.put(
                JpaSettings.INTEGRATOR_PROVIDER,
                BulkAuditIntegratorProvider.class.getName()
        );
    }

    /**
     * Marker bean whose only job is to force {@link BulkAuditBridge} publication before bootstrap.
     */
    public static class BulkAuditBridgePublisher {
    }
}

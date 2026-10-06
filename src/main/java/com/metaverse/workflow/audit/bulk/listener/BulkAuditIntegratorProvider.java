package com.metaverse.workflow.audit.bulk.listener;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.List;

/**
 * Entry point Hibernate uses to pick up the bulk tracking integrator.
 * Named by the {@code hibernate.integrator_provider} property, which is contributed by
 * {@link com.metaverse.workflow.audit.bulk.config.BulkAuditConfig}.
 */
public class BulkAuditIntegratorProvider implements IntegratorProvider {

    @Override
    public List<Integrator> getIntegrators() {
        return List.of(new BulkAuditIntegrator());
    }
}

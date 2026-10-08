package com.metaverse.workflow.audit.bulk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds a flat, JSON friendly snapshot of a Hibernate state array.
 *
 * <p>Walking the state array (instead of serialising the entity with Jackson) keeps the snapshot free of
 * lazy-loading side effects and stops the bidirectional back-references on these entities
 * (files, spiu/agency comments) from recursing forever. Related entities are reduced to their identifier.
 */
@Component
public class BulkSnapshotSerializer {

    private static final Logger log = LoggerFactory.getLogger(BulkSnapshotSerializer.class);

    private static final int MAX_CHANGED_FIELDS_LENGTH = 2048;
    private static final int MAX_SNAPSHOT_LENGTH = 256 * 1024;

    private static final String COLLECTION_MARKER = "<collection>";
    private static final String MAP_MARKER = "<map>";
    private static final String ARRAY_MARKER = "<array>";

    private final ObjectMapper objectMapper;

    public BulkSnapshotSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @return the property name at the given state index, or {@code property[index]} when unknown.
     */
    public String propertyName(EntityPersister persister, int index) {
        String[] names = persister == null ? null : persister.getPropertyNames();
        if (names != null && index >= 0 && index < names.length) {
            return names[index];
        }
        return "property[" + index + "]";
    }

    /**
     * Comma separated list of the property names at the given dirty-property indices.
     */
    public String describeDirtyProperties(EntityPersister persister, int[] dirtyProperties) {
        if (dirtyProperties == null || dirtyProperties.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int index : dirtyProperties) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(propertyName(persister, index));
        }
        String value = sb.toString();
        if (value.length() > MAX_CHANGED_FIELDS_LENGTH) {
            return value.substring(0, MAX_CHANGED_FIELDS_LENGTH);
        }
        return value;
    }

    /**
     * Serialises the given state array into JSON, or null when the state is absent.
     */
    public String toJson(EntityPersister persister, Object[] state, SharedSessionContractImplementor session) {
        if (state == null) {
            return null;
        }
        Map<String, Object> snapshot = new LinkedHashMap<>();
        String[] names = persister == null ? null : persister.getPropertyNames();
        for (int i = 0; i < state.length; i++) {
            String name = (names != null && i < names.length) ? names[i] : ("property[" + i + "]");
            snapshot.put(name, simplify(state[i], session));
        }
        try {
            String json = objectMapper.writeValueAsString(snapshot);
            if (json.length() > MAX_SNAPSHOT_LENGTH) {
                return json.substring(0, MAX_SNAPSHOT_LENGTH) + "...[truncated]";
            }
            return json;
        } catch (Exception e) {
            log.warn("Failed to serialize bulk audit snapshot for {}",
                    persister == null ? "?" : persister.getEntityName(), e);
            return null;
        }
    }

    private Object simplify(Object value, SharedSessionContractImplementor session) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof String
                || value instanceof Enum<?>
                || value instanceof Date
                || value instanceof Temporal
                || value instanceof byte[]) {
            return value instanceof byte[] bytes ? bytes.length + " bytes" : value;
        }
        if (value instanceof Collection<?>) {
            // Collections are never traversed: size() on an uninitialised lazy bag would trigger a
            // load during flush, which is exactly what this snapshot must not cause.
            return COLLECTION_MARKER;
        }
        if (value instanceof Map<?, ?>) {
            return MAP_MARKER;
        }
        if (value.getClass().isArray()) {
            return ARRAY_MARKER;
        }
        return Map.of("id", String.valueOf(identifierOf(value, session)));
    }

    private Object identifierOf(Object value, SharedSessionContractImplementor session) {
        if (value == null) {
            return null;
        }
        try {
            EntityEntry entry = session.getPersistenceContext().getEntry(value);
            if (entry != null && entry.getId() != null) {
                return entry.getId();
            }
        } catch (Exception e) {
            log.trace("Persistence context lookup failed for snapshot", e);
        }
        try {
            Object unproxied = Hibernate.unproxy(value);
            EntityPersister descriptor = session.getFactory().getMappingMetamodel()
                    .getEntityDescriptor(unproxied.getClass());
            if (descriptor != null) {
                return descriptor.getIdentifier(unproxied, session);
            }
        } catch (Exception e) {
            log.trace("Entity descriptor lookup failed for snapshot", e);
        }
        return null;
    }
}

package com.metaverse.workflow.audit.bulk;

import com.metaverse.workflow.model.BulkExpenditure;
import com.metaverse.workflow.model.BulkExpenditureTransaction;

import java.util.Arrays;
import java.util.List;

/**
 * Describes the two tables that are tracked separately from the rest of the application.
 * Nothing here modifies the mapped entities; this enum is only used to recognise them.
 */
public enum BulkTable {

    BULK_EXPENDITURE(
            "bulk_expenditure",
            "bulk_expenditure_history",
            BulkExpenditure.class,
            "bulkExpenditureId",
            List.of("/bulk/expenditure")
    ),

    BULK_EXPENDITURE_TRANSACTION(
            "bulk_expenditure_transaction",
            "bulk_expenditure_transaction_history",
            BulkExpenditureTransaction.class,
            "bulkExpenditureTransactionId",
            List.of("/bulk/transactions", "/save/remarks/transaction")
    );

    private final String tableName;
    private final String historyTableName;
    private final Class<?> entityClass;
    private final String idProperty;
    private final List<String> pathFragments;

    BulkTable(String tableName,
              String historyTableName,
              Class<?> entityClass,
              String idProperty,
              List<String> pathFragments) {
        this.tableName = tableName;
        this.historyTableName = historyTableName;
        this.entityClass = entityClass;
        this.idProperty = idProperty;
        this.pathFragments = pathFragments;
    }

    public String getTableName() {
        return tableName;
    }

    public String getHistoryTableName() {
        return historyTableName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getIdProperty() {
        return idProperty;
    }

    /**
     * @return the request URI fragments that identify endpoints serving this table.
     */
    public List<String> getPathFragments() {
        return pathFragments;
    }

    /**
     * @return true when the request URI belongs to an endpoint that serves this table.
     */
    public boolean matchesPath(String requestUri) {
        if (requestUri == null) {
            return false;
        }
        for (String fragment : pathFragments) {
            if (requestUri.contains(fragment)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the tracked table for a request URI, or null when the endpoint is not tracked.
     */
    public static BulkTable fromRequestPath(String requestUri) {
        if (requestUri == null) {
            return null;
        }
        for (BulkTable table : values()) {
            if (table.matchesPath(requestUri)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Resolves the tracked table for a Hibernate entity name. Returns null when the entity is not tracked.
     */
    public static BulkTable fromEntityName(String entityName) {
        if (entityName == null) {
            return null;
        }
        for (BulkTable table : values()) {
            if (table.entityClass.getName().equals(entityName)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Resolves the tracked table for an entity instance. Returns null when the entity is not tracked.
     */
    public static BulkTable fromEntityClass(Class<?> entityClass) {
        if (entityClass == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(t -> t.entityClass.equals(entityClass))
                .findFirst()
                .orElse(null);
    }
}

package com.metaverse.workflow.audit.bulk.aop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Request/response log for the endpoints that serve {@code bulk_expenditure} and
 * {@code bulk_expenditure_transaction}, modelled on the existing {@code api_logs} table but kept
 * separate so it can be queried on its own.
 *
 * <p>Written by {@link BulkAopLoggingAspect}; never written by application code.
 */
@Entity
@Table(
        name = "bulk_aop_log",
        indexes = {
                @Index(name = "idx_bulk_aop_log_timestamp", columnList = "timestamp"),
                @Index(name = "idx_bulk_aop_log_username", columnList = "username"),
                @Index(name = "idx_bulk_aop_log_table", columnList = "table_name")
        }
)
public class BulkAopLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    /**
     * Entity class the endpoint writes to, e.g. {@code BulkExpenditure}.
     */
    @Column(name = "entity_name", length = 64)
    private String entityName;

    /**
     * Table the endpoint writes to, e.g. {@code bulk_expenditure}.
     */
    @Column(name = "table_name", length = 64)
    private String tableName;

    @Column(name = "module", length = 64)
    private String module;

    @Column(name = "path", length = 1024)
    private String path;

    @Column(name = "http_method", length = 16)
    private String httpMethod;

    @Column(name = "username", length = 128)
    private String username;

    @Lob
    @Column(name = "request_body", columnDefinition = "LONGTEXT")
    private String requestBody;

    @Lob
    @Column(name = "response_body", columnDefinition = "LONGTEXT")
    private String responseBody;

    @Column(name = "error_message", length = 2048)
    private String errorMessage;

    @Column(name = "timestamp")
    private Instant timestamp;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

package com.metaverse.workflow.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "api_logs")
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correlation_id", length = 128)
    private String correlationId;

    @Column(name = "module_name")
    private String module;

    @Column(name = "path", length = 1024)
    private String path;

    @Column(name = "http_method", length = 16)
    private String httpMethod;

    @Column(name = "username")
    private String username;

    @Lob
    @Column(name = "request_body")
    private String requestBody;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "duration_ms")
    private Long durationMs;

    // New column to store extracted identifiers (e.g. program_id, program_expenditure_id, bulk_expenditure_id, transactionId, etc.)
    @Column(name = "identifiers", length = 2000)
    private String identifiers;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public String getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(String identifiers) {
        this.identifiers = identifiers;
    }

}

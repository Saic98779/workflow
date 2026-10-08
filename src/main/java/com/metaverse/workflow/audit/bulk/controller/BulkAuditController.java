package com.metaverse.workflow.audit.bulk.controller;

import com.metaverse.workflow.audit.ApiModule;
import com.metaverse.workflow.audit.bulk.BulkAuditService;
import com.metaverse.workflow.audit.bulk.BulkChangeAction;
import com.metaverse.workflow.audit.bulk.BulkTable;
import com.metaverse.workflow.audit.bulk.aop.BulkAopLog;
import com.metaverse.workflow.audit.bulk.aop.BulkAopLogRepository;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureHistory;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureHistoryRepository;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureTransactionHistory;
import com.metaverse.workflow.audit.bulk.history.BulkExpenditureTransactionHistoryRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Read-only access to everything captured for {@code bulk_expenditure} and
 * {@code bulk_expenditure_transaction}: the request/response log, the row level change history and the
 * per-table counters. Purely additive: nothing here writes to the tracked tables.
 */
@ApiModule("Expenditure")
@RestController
@RequestMapping("/bulk/audit")
public class BulkAuditController {

    private static final int MAX_PAGE_SIZE = 500;

    private final BulkAuditService bulkAuditService;
    private final BulkAopLogRepository bulkAopLogRepository;
    private final BulkExpenditureHistoryRepository expenditureHistoryRepository;
    private final BulkExpenditureTransactionHistoryRepository transactionHistoryRepository;

    public BulkAuditController(BulkAuditService bulkAuditService,
                               BulkAopLogRepository bulkAopLogRepository,
                               BulkExpenditureHistoryRepository expenditureHistoryRepository,
                               BulkExpenditureTransactionHistoryRepository transactionHistoryRepository) {
        this.bulkAuditService = bulkAuditService;
        this.bulkAopLogRepository = bulkAopLogRepository;
        this.expenditureHistoryRepository = expenditureHistoryRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    @GetMapping("/tables")
    public WorkflowResponse tables() {
        return WorkflowResponse.success("Tracked bulk tables", Map.of(
                "tables", Arrays.stream(BulkTable.values())
                        .map(table -> Map.of(
                                "table", table.getTableName(),
                                "historyTable", table.getHistoryTableName(),
                                "aopLogTable", "bulk_aop_log",
                                "idProperty", table.getIdProperty(),
                                "paths", table.getPathFragments()))
                        .toList(),
                "actions", Arrays.stream(BulkChangeAction.values()).map(Enum::name).toList()
        ));
    }

    // ---------- request / response log ----------

    @GetMapping("/logs")
    public WorkflowResponse aopLogs(@RequestParam(required = false) String tableName,
                                    @RequestParam(required = false) String username,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "50") int size) {
        PageRequest pageable = pageable(page, size, "timestamp");
        Page<BulkAopLog> result;
        if (isPresent(tableName)) {
            result = bulkAopLogRepository.findByTableNameOrderByTimestampDesc(tableName, pageable);
        } else if (isPresent(username)) {
            result = bulkAopLogRepository.findByUsernameOrderByTimestampDesc(username, pageable);
        } else {
            result = bulkAopLogRepository.findAllByOrderByTimestampDesc(pageable);
        }
        return paged("bulk_aop_log", result);
    }

    @GetMapping("/logs/range")
    public WorkflowResponse aopLogsBetween(@RequestParam String from,
                                           @RequestParam String to,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "50") int size) {
        Page<BulkAopLog> result = bulkAopLogRepository.findByTimestampBetweenOrderByTimestampAsc(
                parseInstant(from), parseInstant(to), pageable(page, size, "timestamp"));
        return paged("bulk_aop_log in range", result);
    }

    // ---------- row level history ----------

    @GetMapping("/expenditure/{recordId}")
    public WorkflowResponse expenditureHistory(@PathVariable Long recordId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        return paged("bulk_expenditure history", expenditureHistoryRepository
                .findByRecordIdOrderByChangedAtDesc(recordId, pageable(page, size, "changedAt")));
    }

    @GetMapping("/transaction/{recordId}")
    public WorkflowResponse transactionHistory(@PathVariable Long recordId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        return paged("bulk_expenditure_transaction history", transactionHistoryRepository
                .findByRecordIdOrderByChangedAtDesc(recordId, pageable(page, size, "changedAt")));
    }

    @GetMapping("/expenditure/changes")
    public WorkflowResponse expenditureChanges(@RequestParam(required = false) String action,
                                              @RequestParam(required = false) String changedBy,
                                              @RequestParam(required = false) String correlationId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        PageRequest pageable = pageable(page, size, "changedAt");
        Page<BulkExpenditureHistory> result;
        if (isPresent(action)) {
            result = expenditureHistoryRepository.findByActionOrderByChangedAtDesc(action, pageable);
        } else if (isPresent(changedBy)) {
            result = expenditureHistoryRepository.findByChangedByOrderByChangedAtDesc(changedBy, pageable);
        } else if (isPresent(correlationId)) {
            result = expenditureHistoryRepository.findByCorrelationIdOrderByChangedAtAsc(correlationId, pageable);
        } else {
            result = expenditureHistoryRepository.findAllByOrderByChangedAtDesc(pageable);
        }
        return paged("bulk_expenditure changes", result);
    }

    @GetMapping("/transaction/changes")
    public WorkflowResponse transactionChanges(@RequestParam(required = false) String action,
                                              @RequestParam(required = false) String changedBy,
                                              @RequestParam(required = false) String correlationId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "50") int size) {
        PageRequest pageable = pageable(page, size, "changedAt");
        Page<BulkExpenditureTransactionHistory> result;
        if (isPresent(action)) {
            result = transactionHistoryRepository.findByActionOrderByChangedAtDesc(action, pageable);
        } else if (isPresent(changedBy)) {
            result = transactionHistoryRepository.findByChangedByOrderByChangedAtDesc(changedBy, pageable);
        } else if (isPresent(correlationId)) {
            result = transactionHistoryRepository.findByCorrelationIdOrderByChangedAtAsc(correlationId, pageable);
        } else {
            result = transactionHistoryRepository.findAllByOrderByChangedAtDesc(pageable);
        }
        return paged("bulk_expenditure_transaction changes", result);
    }

    // ---------- counters ----------

    @GetMapping("/counters")
    public WorkflowResponse counters() {
        return WorkflowResponse.success("Bulk table counters", bulkAuditService.getCounters());
    }

    @PostMapping("/counters/resync")
    public WorkflowResponse resyncCounters() {
        return WorkflowResponse.success("Bulk table counters resynchronised",
                bulkAuditService.resynchroniseCounters());
    }

    @GetMapping("/summary")
    public WorkflowResponse summary() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("counters", bulkAuditService.getCounters());
        data.put("bulkAopLogRows", bulkAopLogRepository.count());
        data.put("bulkExpenditureHistoryRows", bulkAuditService.getExpenditureHistoryCount());
        data.put("bulkExpenditureTransactionHistoryRows", bulkAuditService.getTransactionHistoryCount());
        return WorkflowResponse.success("Bulk audit summary", data);
    }

    private Instant parseInstant(String value) {
        return Instant.parse(value);
    }

    private boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    private PageRequest pageable(int page, int size, String sortProperty) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, sortProperty));
    }

    private <T> WorkflowResponse paged(String message, Page<T> page) {
        return WorkflowResponse.builder()
                .status(200)
                .message(message)
                .data(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}

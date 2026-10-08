package com.metaverse.workflow.audit.bulk.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.audit.ApiModule;
import com.metaverse.workflow.audit.bulk.BulkAuditContext;
import com.metaverse.workflow.audit.bulk.BulkTable;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Instant;

/**
 * Separate request/response logging for the endpoints that serve {@code bulk_expenditure} and
 * {@code bulk_expenditure_transaction}, written to {@code bulk_aop_log}.
 *
 * <p>Modelled on the existing {@code ApiLoggingAspect} but narrowed to the bulk expenditure routes, so
 * the existing {@code api_logs} behaviour is completely unaffected. The controller is only wrapped, never
 * modified, and the request is mapped to a table by {@link BulkTable#fromRequestPath}.
 */
@Aspect
@Component
public class BulkAopLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(BulkAopLoggingAspect.class);

    private static final int MAX_BODY_LENGTH = 32 * 1024;

    private final BulkAopLogService bulkAopLogService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BulkAopLoggingAspect(BulkAopLogService bulkAopLogService, ObjectMapper objectMapper) {
        this.bulkAopLogService = bulkAopLogService;
        this.objectMapper = objectMapper;
    }

    @Around("within(com.metaverse.workflow.expenditure.controller.ExpenditureController)")
    public Object logBulkRequest(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = currentRequest();
        BulkTable table = BulkTable.fromRequestPath(request == null ? null : request.getRequestURI());
        if (table == null) {
            return pjp.proceed();
        }

        Object result = null;
        Throwable failure = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable throwable) {
            failure = throwable;
            throw throwable;
        } finally {
            try {
                persist(pjp, table, request, result, failure);
            } catch (Exception e) {
                log.error("Failed to create bulk aop log", e);
            }
        }
    }

    private void persist(ProceedingJoinPoint pjp,
                        BulkTable table,
                        HttpServletRequest request,
                        Object result,
                        Throwable failure) {
        BulkAopLog bulkAopLog = new BulkAopLog();
        bulkAopLog.setEntityName(table.getEntityClass().getSimpleName());
        bulkAopLog.setTableName(table.getTableName());
        bulkAopLog.setModule(resolveModule(pjp));
        bulkAopLog.setPath(request == null ? null : request.getRequestURI());
        bulkAopLog.setHttpMethod(request == null ? null : request.getMethod());
        bulkAopLog.setUsername(resolveUsername(pjp, request));
        bulkAopLog.setRequestBody(truncate(requestBody(pjp, request)));
        bulkAopLog.setResponseBody(truncate(responseBody(result)));
        bulkAopLog.setErrorMessage(failure == null ? null : truncate(failure.getMessage()));
        bulkAopLog.setTimestamp(Instant.now());

        bulkAopLogService.saveAsync(bulkAopLog);
    }

    private String resolveModule(ProceedingJoinPoint pjp) {
        try {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            ApiModule module = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ApiModule.class);
            return module == null ? null : module.value();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prefers the {@link Principal} argument these endpoints already receive, then the servlet request,
     * then the ambient security context.
     */
    private String resolveUsername(ProceedingJoinPoint pjp, HttpServletRequest request) {
        Object[] args = pjp.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof Principal principal
                        && principal.getName() != null
                        && !principal.getName().isBlank()) {
                    return principal.getName();
                }
            }
        }
        if (request != null && request.getUserPrincipal() != null) {
            return request.getUserPrincipal().getName();
        }
        String resolved = BulkAuditContext.resolveUsername(args);
        return "anonymousUser".equals(resolved) ? null : resolved;
    }

    /**
     * These endpoints receive their payload either as a JSON {@code @RequestPart String} (multipart)
     * or as a {@code @RequestBody}. Both are handled; uploaded files are never included.
     */
    private String requestBody(ProceedingJoinPoint pjp, HttpServletRequest request) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        java.lang.annotation.Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        boolean annotated = parameterAnnotations.length == args.length;

        for (int i = 0; annotated && i < args.length; i++) {
            if (args[i] == null) {
                continue;
            }
            for (java.lang.annotation.Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestPart) {
                    return args[i] instanceof String json ? json : writeValue(args[i]);
                }
            }
        }

        String cached = cachedRequestBody(request);
        if (cached != null && !cached.isBlank()) {
            return cached;
        }

        for (int i = 0; annotated && i < args.length; i++) {
            if (args[i] == null || args[i] instanceof MultipartFile || args[i] instanceof MultipartFile[]) {
                continue;
            }
            for (java.lang.annotation.Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestBody) {
                    return writeValue(args[i]);
                }
            }
        }
        return cached;
    }

    private String cachedRequestBody(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper == null) {
            return null;
        }
        byte[] buffer = wrapper.getContentAsByteArray();
        if (buffer.length == 0) {
            return null;
        }
        return new String(buffer, StandardCharsets.UTF_8);
    }

    /**
     * The content-caching response wrapper is only filled once the whole filter chain has run, so at
     * aspect time the controller return value is the reliable source.
     */
    private String responseBody(Object result) {
        if (result == null) {
            return null;
        }
        Object payload = result instanceof ResponseEntity<?> responseEntity ? responseEntity.getBody() : result;
        if (payload == null) {
            return null;
        }
        if (payload instanceof String text) {
            return text;
        }
        return writeValue(payload);
    }

    private String writeValue(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.debug("Failed to serialize bulk aop body", e);
            return null;
        }
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() > MAX_BODY_LENGTH) {
            return value.substring(0, MAX_BODY_LENGTH) + "...[truncated]";
        }
        return value;
    }
}

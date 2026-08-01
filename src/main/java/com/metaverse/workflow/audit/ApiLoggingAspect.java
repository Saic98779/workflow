package com.metaverse.workflow.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.audit.filter.RequestResponseWrappingFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingAspect.class);

    private final ApiLogService apiLogService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApiLoggingAspect(ApiLogService apiLogService, ObjectMapper objectMapper) {
        this.apiLogService = apiLogService;
        this.objectMapper = objectMapper;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logApi(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Exception toThrow = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Exception ex) {
            toThrow = ex;
            throw ex;
        } finally {
            try {
                long duration = System.currentTimeMillis() - start;
                ApiLog apiLog = new ApiLog();
                String path = "";
                String method = "";
                String username = null;
                String requestBody = null;
                String responseBody = null;

                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (requestAttributes instanceof ServletRequestAttributes) {
                    ServletRequestAttributes servletAttrs = (ServletRequestAttributes) requestAttributes;
                    HttpServletRequest httpServletRequest = servletAttrs.getRequest();
                    HttpServletResponse httpServletResponse = servletAttrs.getResponse();

                    if (httpServletRequest != null) {
                        path = httpServletRequest.getRequestURI();
                        method = httpServletRequest.getMethod();
                        if (httpServletRequest.getUserPrincipal() != null) {
                            username = httpServletRequest.getUserPrincipal().getName();
                        }

                        if (httpServletRequest instanceof org.springframework.web.util.ContentCachingRequestWrapper) {
                            org.springframework.web.util.ContentCachingRequestWrapper wrapper = (org.springframework.web.util.ContentCachingRequestWrapper) httpServletRequest;
                            byte[] buf = wrapper.getContentAsByteArray();
                            if (buf.length > 0) {
                                requestBody = new String(buf, StandardCharsets.UTF_8);
                            }
                        }

                        if (httpServletResponse != null && httpServletResponse instanceof org.springframework.web.util.ContentCachingResponseWrapper) {
                            org.springframework.web.util.ContentCachingResponseWrapper respWrapper = (org.springframework.web.util.ContentCachingResponseWrapper) httpServletResponse;
                            byte[] buf = respWrapper.getContentAsByteArray();
                            if (buf.length > 0) {
                                responseBody = new String(buf, StandardCharsets.UTF_8);
                            }
                        }

                        // Extract identifiers and set into apiLog
                        try {
                            String identifiers = extractIdentifiers(httpServletRequest, requestBody);
                            apiLog.setIdentifiers(truncate(identifiers));
                        } catch (Exception e) {
                            log.debug("Failed to extract identifiers", e);
                        }
                    }
                }

                if (responseBody == null) {
                    try {
                        responseBody = objectMapper.writeValueAsString(result);
                    } catch (Exception e) {
                        responseBody = String.valueOf(result);
                    }
                }

                // Skip saving logs for the swagger-config endpoint (covers both with and without context path)
                boolean skipSwaggerConfig = path != null && path.contains("/v3/api-docs/swagger-config");
                if (skipSwaggerConfig) {
                    log.debug("Skipping api log for swagger-config path: {}", path);
                } else {
                    apiLog.setPath(path);
                    apiLog.setHttpMethod(method);
                    apiLog.setUsername(username);
                    apiLog.setRequestBody(truncate(requestBody));
                    apiLog.setResponseBody(truncate(responseBody));
                    apiLog.setTimestamp(Instant.now());
                    apiLog.setDurationMs(duration);

                    // correlation id from header or MDC
                    String correlationId = null;
                    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
                    if (ra instanceof ServletRequestAttributes) {
                        HttpServletRequest req = ((ServletRequestAttributes) ra).getRequest();
                        if (req != null) {
                            correlationId = req.getHeader(RequestResponseWrappingFilter.CORRELATION_ID_HEADER);
                        }
                    }
                    apiLog.setCorrelationId(correlationId);

                    // module name from controller annotation if present
                    MethodSignature signature = (MethodSignature) pjp.getSignature();
                    Method methodRef = signature.getMethod();
                    Class<?> declaring = methodRef.getDeclaringClass();
                    ApiModule module = AnnotationUtils.findAnnotation(declaring, ApiModule.class);
                    if (module != null) {
                        apiLog.setModule(module.value());
                    }

                    // save asynchronously
                    apiLogService.saveAsync(apiLog);
                }

            } catch (Exception e) {
                log.error("Failed to create api log", e);
            }
            if (toThrow != null) {
                // rethrow original
                throw toThrow;
            }
        }
    }

    private String truncate(String s) {
        if (s == null) return null;
        int max = 32 * 1024; // 32KB
        if (s.length() > max) return s.substring(0, max) + "...[truncated]";
        return s;
    }

    private String extractIdentifiers(HttpServletRequest request, String requestBody) {
        Map<String, String> ids = new LinkedHashMap<>();

        // 1) Query parameters (includes form parameters and query string)
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
                String key = e.getKey();
                if (key == null) continue;
                String lower = key.toLowerCase(Locale.ROOT);
                if (looksLikeIdKey(lower)) {
                    String[] vals = e.getValue();
                    if (vals != null && vals.length > 0 && vals[0] != null && !vals[0].isEmpty()) {
                        ids.put(key, vals[0]);
                    }
                }
            }
        } catch (Exception ignored) {
        }

        // 2) Path variables: pick numeric path segments and pair them with preceding segment
        try {
            String path = request.getRequestURI();
            if (path != null) {
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i];
                    if (part == null || part.isEmpty()) continue;
                    // numeric id (long)
                    if (part.matches("\\d+")) {
                        String key = "id";
                        if (i > 0) {
                            String prev = parts[i - 1];
                            if (prev != null && !prev.isEmpty()) {
                                key = normalizeKey(prev);
                                // make sure key names contain "id"
                                if (!key.toLowerCase(Locale.ROOT).endsWith("id") && !key.toLowerCase(Locale.ROOT).contains("id")) {
                                    key = key + "Id";
                                }
                            }
                        }
                        if (!ids.containsKey(key)) {
                            ids.put(key, part);
                        } else {
                            // avoid overwriting existing, append index
                            ids.put(key + "_" + UUID.randomUUID().toString().substring(0, 4), part);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        // 3) JSON request body: search recursively for id-like keys
        if (requestBody != null && !requestBody.isBlank()) {
            try {
                Object parsed = objectMapper.readValue(requestBody, Object.class);
                if (parsed instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) parsed;
                    collectIdsFromMap(map, ids, null);
                }
            } catch (Exception ignored) {
                // not JSON or parse failed; ignore
            }
        }

        if (ids.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(ids);
        } catch (Exception e) {
            // fallback to simple join
            StringBuilder sb = new StringBuilder();
            ids.forEach((k, v) -> sb.append(k).append("=").append(v).append(";"));
            return sb.toString();
        }
    }

    private boolean looksLikeIdKey(String lower) {
        if (lower == null) return false;
        return lower.equals("id")
                || lower.endsWith("id")
                || lower.contains("expenditure")
                || lower.contains("transaction")
                || lower.contains("program")
                || lower.contains("bulk")
                || lower.contains("agency")
                || lower.contains("participant")
                || lower.contains("vendor");
    }

    private void collectIdsFromMap(Map<String, Object> map, Map<String, String> ids, String parentKey) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if (key == null) continue;
            String lower = key.toLowerCase(Locale.ROOT);
            if (value == null) continue;
            if (value instanceof Number || (value instanceof String && ((String) value).matches("\\d+"))) {
                if (looksLikeIdKey(lower) || lower.endsWith("_id") || lower.endsWith("id")) {
                    String k = key;
                    if (parentKey != null && (k == null || k.isEmpty())) k = parentKey + "Id";
                    ids.putIfAbsent(k, String.valueOf(value));
                }
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nested = (Map<String, Object>) value;
                collectIdsFromMap(nested, ids, key);
            } else if (value instanceof Collection) {
                Collection<?> coll = (Collection<?>) value;
                int idx = 0;
                for (Object item : coll) {
                    if (item instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> nested = (Map<String, Object>) item;
                        collectIdsFromMap(nested, ids, key + "[" + idx + "]");
                    }
                    idx++;
                }
            }
        }
    }

    private String normalizeKey(String raw) {
        if (raw == null) return "id";
        String k = raw.replaceAll("[^A-Za-z0-9]", "");
        if (k.isEmpty()) return "id";
        // convert hyphen/camel cases to lowerCamelId
        if (!k.toLowerCase(Locale.ROOT).endsWith("id")) {
            // try to keep original casing but append Id
            return k;
        }
        return k;
    }

}

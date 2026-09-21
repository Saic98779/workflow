package com.metaverse.workflow.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.audit.filter.RequestResponseWrappingFilter;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.WebUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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

                        // Request body: prefer the exact bytes cached by the wrapping filter, but fall back to
                        // serializing the actual @RequestBody argument so the payload is never lost.
                        ContentCachingRequestWrapper cachingRequest =
                                WebUtils.getNativeRequest(httpServletRequest, ContentCachingRequestWrapper.class);
                        if (cachingRequest != null) {
                            byte[] buf = cachingRequest.getContentAsByteArray();
                            // Only treat it as a textual body for non-multipart requests; multipart bodies are
                            // consumed by the container and rarely cached by the wrapper.
                            if (buf.length > 0 && isTextualContentType(httpServletRequest.getContentType())) {
                                requestBody = new String(buf, StandardCharsets.UTF_8);
                            }
                        }
                        if (requestBody == null || requestBody.isEmpty()) {
                            requestBody = serializeRequestBody(pjp);
                        }

                        // NOTE: ContentCachingResponseWrapper is only populated AFTER the full filter chain
                        // (including HTTP message converter writing) completes, so at aspect time its buffer is
                        // normally empty. It is kept as a fallback for controllers that write directly to the
                        // response; otherwise the controller return value is serialized below.
                        ContentCachingResponseWrapper cachingResponse =
                                WebUtils.getNativeResponse(httpServletResponse, ContentCachingResponseWrapper.class);
                        if (cachingResponse != null) {
                            byte[] buf = cachingResponse.getContentAsByteArray();
                            if (buf.length > 0 && isTextualContentType(httpServletResponse.getContentType())) {
                                responseBody = new String(buf, StandardCharsets.UTF_8);
                            }
                        }

                    }
                }

                if (responseBody == null) {
                    Object payload = unwrapResult(result);
                    if (payload != null) {
                        try {
                            responseBody = objectMapper.writeValueAsString(payload);
                        } catch (Exception e) {
                            log.debug("Failed to serialize response body", e);
                        }
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

    private String serializeRequestBody(ProceedingJoinPoint pjp) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Object[] args = pjp.getArgs();
            java.lang.annotation.Annotation[][] paramAnnotations = signature.getMethod().getParameterAnnotations();
            if (paramAnnotations.length != args.length) return null;

            // Prefer the argument annotated with @RequestBody
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) continue;
                for (java.lang.annotation.Annotation annotation : paramAnnotations[i]) {
                    if (annotation instanceof org.springframework.web.bind.annotation.RequestBody) {
                        return objectMapper.writeValueAsString(args[i]);
                    }
                }
            }

            // Otherwise serialize the remaining payload-style arguments. This covers endpoints that receive
            // their payload differently, e.g. a JSON string passed as @RequestParam together with multipart files.
            List<Object> payloads = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null || isFrameworkArgument(arg)) continue;
                if (arg instanceof String) {
                    String value = ((String) arg).trim();
                    if (value.startsWith("{") || value.startsWith("[")) {
                        payloads.add(arg);
                    }
                    continue;
                }
                if (isScalarArgument(arg)) continue;
                payloads.add(arg);
            }
            if (payloads.isEmpty()) return null;
            if (payloads.size() == 1) {
                Object only = payloads.get(0);
                if (only instanceof String) return (String) only;
                return objectMapper.writeValueAsString(only);
            }
            return objectMapper.writeValueAsString(payloads);
        } catch (Exception e) {
            log.debug("Failed to serialize request body", e);
            return null;
        }
    }

    private boolean isFrameworkArgument(Object arg) {
        return arg instanceof jakarta.servlet.ServletRequest
                || arg instanceof jakarta.servlet.ServletResponse
                || arg instanceof jakarta.servlet.http.HttpSession
                || arg instanceof java.security.Principal
                || arg instanceof Locale
                || arg instanceof org.springframework.validation.BindingResult
                || arg instanceof org.springframework.validation.Errors
                || arg instanceof org.springframework.web.context.request.WebRequest
                || arg instanceof org.springframework.web.multipart.MultipartFile
                || arg instanceof org.springframework.web.multipart.MultipartFile[]
                || arg instanceof org.springframework.web.bind.support.SessionStatus
                || arg instanceof org.springframework.ui.Model
                || arg instanceof org.springframework.ui.ModelMap;
    }

    private boolean isScalarArgument(Object arg) {
        return arg instanceof Number
                || arg instanceof Boolean
                || arg instanceof Character
                || arg instanceof Enum<?>
                || arg instanceof java.time.temporal.Temporal
                || arg instanceof java.util.Date;
    }

    private Object unwrapResult(Object result) {
        if (result instanceof ResponseEntity<?>) {
            return ((ResponseEntity<?>) result).getBody();
        }
        if (result instanceof org.springframework.http.HttpEntity<?>) {
            return ((org.springframework.http.HttpEntity<?>) result).getBody();
        }
        return result;
    }

    private boolean isTextualContentType(String contentType) {
        if (contentType == null) return false;
        String lower = contentType.toLowerCase(Locale.ROOT);
        return lower.startsWith("application/json")
                || lower.startsWith("text/")
                || lower.startsWith("application/xml")
                || lower.startsWith("application/x-www-form-urlencoded");
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

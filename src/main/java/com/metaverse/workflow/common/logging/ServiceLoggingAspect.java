package com.metaverse.workflow.common.logging;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String requestLog = Arrays.stream(joinPoint.getArgs())
                .map(this::formatValue)
                .reduce((left, right) -> left + ", " + right)
                .orElse("no-args");

        log.info("Service request: {} args=[{}]", methodName, requestLog);

        try {
            Object response = joinPoint.proceed();
            log.info("Service response: {} result={}", methodName, formatValue(response));
            return response;
        } catch (Throwable ex) {
            log.error("Service error: {} message={}", methodName, ex.getMessage(), ex);
            throw ex;
        }
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof MultipartFile file) {
            return "MultipartFile(name=" + file.getOriginalFilename() + ", size=" + file.getSize() + ")";
        }
        if (value instanceof ServletRequest || value instanceof ServletResponse) {
            return value.getClass().getSimpleName();
        }
        if (value instanceof InputStream || value instanceof OutputStream || value instanceof Reader || value instanceof Writer) {
            return value.getClass().getSimpleName();
        }
        if (value instanceof byte[] bytes) {
            return "byte[" + bytes.length + "]";
        }
        if (value instanceof Collection<?> collection) {
            return "Collection(size=" + collection.size() + ", values=" + collection + ")";
        }
        if (value instanceof Map<?, ?> map) {
            return "Map(size=" + map.size() + ", values=" + map + ")";
        }
        return String.valueOf(value);
    }
}
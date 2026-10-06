package com.metaverse.workflow.audit.bulk;

import com.metaverse.workflow.audit.filter.RequestResponseWrappingFilter;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.function.Supplier;

/**
 * Holds the acting-user details for the current thread while a bulk expenditure operation runs.
 * The Hibernate listeners read this to enrich the history rows; it is populated by {@link BulkAuditAspect}
 * and always falls back to the ambient security context when it is empty.
 */
public final class BulkAuditContext {

    private static final ThreadLocal<Snapshot> CURRENT = new ThreadLocal<>();

    private BulkAuditContext() {
    }

    public static void set(String username, String correlationId, String sourceMethod) {
        CURRENT.set(new Snapshot(username, correlationId, sourceMethod));
    }

    public static void clear() {
        CURRENT.remove();
    }

    public static boolean isActive() {
        return CURRENT.get() != null;
    }

    /**
     * @return the username stamped on this thread, or null when nothing was stamped.
     */
    public static String username() {
        Snapshot snapshot = CURRENT.get();
        return snapshot == null ? null : snapshot.username();
    }

    /**
     * @return the correlation id of the request, falling back to the MDC value set by the request filter.
     */
    public static String correlationId() {
        Snapshot snapshot = CURRENT.get();
        if (snapshot != null && snapshot.correlationId() != null) {
            return snapshot.correlationId();
        }
        return MDC.get(RequestResponseWrappingFilter.MDC_CORRELATION_ID);
    }

    /**
     * @return the service method that triggered the change, or null when unknown.
     */
    public static String sourceMethod() {
        Snapshot snapshot = CURRENT.get();
        return snapshot == null ? null : snapshot.sourceMethod();
    }

    /**
     * Best-effort username for a history row: the value stamped by the aspect, then any {@link Principal}
     * argument, then the Spring Security authentication, then "system".
     */
    public static String resolveUsername(Object[] args) {
        String stamped = username();
        if (isNotBlank(stamped)) {
            return stamped;
        }
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof Principal principal
                        && principal.getName() != null
                        && !principal.getName().isBlank()) {
                    return principal.getName();
                }
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && isNotBlank(authentication.getName())) {
            return authentication.getName();
        }
        return "system";
    }

    /**
     * Runs the supplier with an explicit username, restoring any previous context afterwards.
     */
    public static <T> T runAs(String username, String sourceMethod, Supplier<T> action) {
        Snapshot previous = CURRENT.get();
        set(username, correlationId(), sourceMethod);
        try {
            return action.get();
        } finally {
            if (previous == null) {
                clear();
            } else {
                CURRENT.set(previous);
            }
        }
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private record Snapshot(String username, String correlationId, String sourceMethod) {
    }
}

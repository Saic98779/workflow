package com.metaverse.workflow.audit.bulk;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Stamps the acting user onto the current thread for the duration of a bulk expenditure write, so the
 * Hibernate listeners can attribute each captured row change to a person.
 *
 * <p>This is additive only: it wraps the expenditure service methods without changing their behaviour,
 * signatures or annotations, and it is purely thread-local state. Reads are left alone because they
 * produce no history rows.
 */
@Aspect
@Component
public class BulkAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(BulkAuditAspect.class);

    @Around("execution(* com.metaverse.workflow.expenditure.service.ExpenditureServiceAdepter.save*(..))"
            + " || execution(* com.metaverse.workflow.expenditure.service.ExpenditureServiceAdepter.update*(..))"
            + " || execution(* com.metaverse.workflow.expenditure.service.ExpenditureServiceAdepter.delete*(..))"
            + " || execution(* com.metaverse.workflow.expenditure.service.ExpenditureServiceAdepter.addRemark*(..))")
    public Object stampActingUser(ProceedingJoinPoint pjp) throws Throwable {
        if (BulkAuditContext.isActive()) {
            return pjp.proceed();
        }
        try {
            BulkAuditContext.set(resolveUsername(pjp.getArgs()), null, methodName(pjp));
            return pjp.proceed();
        } finally {
            BulkAuditContext.clear();
        }
    }

    private String methodName(ProceedingJoinPoint pjp) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            return signature.getDeclaringType().getSimpleName() + "#" + signature.getName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String resolveUsername(Object[] args) {
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
        if (authentication != null
                && authentication.getName() != null
                && !authentication.getName().isBlank()
                && !"anonymousUser".equals(authentication.getName())) {
            return authentication.getName();
        }
        log.trace("No authenticated principal found for a bulk write; recording as system");
        return "system";
    }
}

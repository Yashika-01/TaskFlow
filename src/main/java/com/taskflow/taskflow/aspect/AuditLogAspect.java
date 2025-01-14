package com.taskflow.taskflow.aspect;

import com.taskflow.taskflow.service.AuditLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogService auditService;

    @Autowired
    public AuditLogAspect(AuditLogService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("execution(* com.taskflow.taskflow.controller.*.*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logAudit(JoinPoint joinPoint) {
        // Fetch the logged-in username
        String username = "Anonymous";

        // Get the method name (action)
        String action = joinPoint.getSignature().getName();

        // Get the class name (resource)
        String resource = joinPoint.getTarget().getClass().getSimpleName();

        // Get the HTTP method (GET, POST, etc.)
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String method = request.getMethod();

        // Get the IP address of the client
        String ipAddress = request.getRemoteAddr();

        // Get method arguments
        Object[] args = joinPoint.getArgs();
        String methodArguments = Arrays.toString(args);

        // Log the action
        auditService.logAction(username, action, resource, method, ipAddress, methodArguments);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String username = "Anonymous";

        String action = joinPoint.getSignature().getName();
        String resource = joinPoint.getTarget().getClass().getSimpleName();

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String exceptionMessage = exception.getMessage();

        auditService.logException(username, action, resource, method, ipAddress, exceptionMessage);
    }

}

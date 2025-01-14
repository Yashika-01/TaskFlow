package com.taskflow.taskflow.service;

import com.taskflow.taskflow.model.AuditLog;
import com.taskflow.taskflow.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // Log successful action
    public void logAction(String username, String action, String resource, String method, String ipAddress, String methodArguments) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setResource(resource);
        auditLog.setMethod(method);
        auditLog.setIpAddress(ipAddress);
        auditLog.setTimestamp(new Date());
        auditLog.setMethodArguments(methodArguments);  // Save method arguments as well

        auditLogRepository.save(auditLog);
    }

    // Log exceptions thrown during method execution
    public void logException(String username, String action, String resource, String method, String ipAddress, String exceptionMessage) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(action);
        auditLog.setResource(resource);
        auditLog.setMethod(method);
        auditLog.setIpAddress(ipAddress);
        auditLog.setTimestamp(new Date());
        auditLog.setExceptionMessage(exceptionMessage);  // Save the exception message

        auditLogRepository.save(auditLog);
    }
}

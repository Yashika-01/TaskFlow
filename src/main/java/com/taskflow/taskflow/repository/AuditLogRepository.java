package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

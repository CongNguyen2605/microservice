package org.example.auditservice.entity.elastic_search.service;

import java.util.Map;

public interface AuditLogService {
    void saveAudit(String service, String action, String userId, Map<String, Object> payload);
}

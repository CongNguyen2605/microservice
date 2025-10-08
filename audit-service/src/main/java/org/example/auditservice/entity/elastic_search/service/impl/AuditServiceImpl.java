package org.example.auditservice.entity.elastic_search.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auditservice.entity.elastic_search.AuditLog;
import org.example.auditservice.entity.elastic_search.AuditLogRepository;
import org.example.auditservice.entity.elastic_search.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;
    @Override
    public void saveAudit(String service, String action, String userId, Map<String, Object> payload) {
        AuditLog log = new AuditLog();
        log.setServiceName(service);
        log.setActionType(action);
        log.setUserId(userId);
        log.setPayload(payload);
        log.setTimestamp(java.time.Instant.now());
        repository.save(log);
    }
}

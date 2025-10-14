package org.example.auditservice.entity.elastic_search.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.auditservice.dto.audit_aggregate.AuditAggregateDto;
import org.example.auditservice.entity.elastic_search.AuditLog;
import org.example.auditservice.entity.elastic_search.AuditLogRepository;
import org.example.auditservice.entity.elastic_search.Payload;
import org.example.auditservice.entity.elastic_search.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;
    @Override
    public void saveAudit(String service, String action, String userId, Payload payload) {
        AuditLog log = new AuditLog();
        log.setServiceName(service);
        log.setActionType(action);
        log.setUserId(userId);
        log.setPayload(payload);
        log.setTimestamp(Instant.now().toEpochMilli());
        repository.save(log);
    }

    @Override
    public Page<AuditAggregateDto> findAll(Pageable pageable) {
        Page<AuditLog> auditLogs = repository.findAll(pageable);

        return auditLogs.map(auditLog -> {
            AuditAggregateDto dto = new AuditAggregateDto();
            dto.setDatabase(auditLog.getServiceName());
            dto.setTimestamp(auditLog.getTimestamp().toString());
            dto.setBeforeValues(auditLog.getPayload().getBefore());
            dto.setAfterValues( auditLog.getPayload().getAfter());
            return dto;
        });
    }
}

package org.example.auditservice.entity.elastic_search.service;

import org.example.auditservice.dto.audit_aggregate.AuditAggregateDto;
import org.example.auditservice.entity.elastic_search.Payload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AuditLogService {
    void saveAudit(String service, String action, String userId, Payload payload);
    Page<AuditAggregateDto> findAll(Pageable pageable);
}

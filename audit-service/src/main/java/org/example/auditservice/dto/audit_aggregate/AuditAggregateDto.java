package org.example.auditservice.dto.audit_aggregate;

import lombok.Data;

import java.util.Map;
@Data
public class AuditAggregateDto {
    private String database;
    private String timestamp;
    private Map<String, Object> beforeValues;
    private Map<String, Object> afterValues;
}

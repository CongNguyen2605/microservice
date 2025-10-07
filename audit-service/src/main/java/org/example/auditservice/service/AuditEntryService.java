package org.example.auditservice.service;

import org.example.auditservice.dto.audit_entry.AuditEntryDto;

public interface AuditEntryService {
    void create(AuditEntryDto auditEntryDto);
}

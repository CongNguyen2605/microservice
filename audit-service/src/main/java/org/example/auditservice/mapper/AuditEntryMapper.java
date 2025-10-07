package org.example.auditservice.mapper;

import org.example.auditservice.dto.audit_entry.AuditEntryDto;
import org.example.auditservice.entity.AuditEntryEntity;
import org.example.auditservice.entity.AuditEntryLatestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditEntryMapper {
    AuditEntryLatestEntity toEntity(AuditEntryDto auditEntryDto);

    AuditEntryEntity toEntry(AuditEntryDto auditEntryDto);
}

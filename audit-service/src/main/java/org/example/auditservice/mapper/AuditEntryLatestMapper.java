package org.example.auditservice.mapper;

import org.example.auditservice.dto.audit_entry.AuditEntryDto;
import org.example.auditservice.entity.AuditEntryLatestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuditEntryLatestMapper {
    AuditEntryLatestEntity toEntity(AuditEntryDto auditEntryDto);
    @Mapping(target = "id", ignore = true)
    void toUpdate( AuditEntryLatestEntity saveAuditEntryLatest,@MappingTarget AuditEntryLatestEntity auditEntryLatest);
}

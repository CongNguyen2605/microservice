package org.example.auditservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.auditservice.dto.audit_entry.AuditEntryDto;
import org.example.auditservice.entity.AuditEntryEntity;
import org.example.auditservice.entity.AuditEntryLatestEntity;
import org.example.auditservice.entity.TableConfigurationEntity;
import org.example.auditservice.mapper.AuditEntryLatestMapper;
import org.example.auditservice.mapper.AuditEntryMapper;
import org.example.auditservice.repository.AuditEntryLatestRepository;
import org.example.auditservice.repository.AuditEntryRepository;
import org.example.auditservice.repository.TableConfigurationRepository;
import org.example.auditservice.service.AuditEntryService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditEntryServiceImpl implements AuditEntryService {
    private final AuditEntryRepository auditEntryRepository;
    private final AuditEntryMapper auditEntryMapper;
    private final TableConfigurationRepository tableConfigurationRepository;
    private final AuditEntryLatestRepository auditEntryLatestRepository;
    private final AuditEntryLatestMapper auditEntryLatestMapper;

    @Override
    public void create(AuditEntryDto auditEntryDto) {
        String database = auditEntryDto.getTableIdentifier().getDatabase();
        String schema = auditEntryDto.getTableIdentifier().getSchema();
        if ("unknown".equalsIgnoreCase(schema)) {
            schema = null;
        }
        String tableName = auditEntryDto.getTableIdentifier().getTableName();

        TableConfigurationEntity tableConfig = (schema == null || schema.isBlank())
                ? tableConfigurationRepository.findByTableNameAndSchemaName(database, tableName).orElse(null)
                : tableConfigurationRepository.findByDatabaseNameAndSchemaNameAndTableName(database, schema, tableName)
                .orElse(null);

        if (tableConfig == null) {
            throw new IllegalStateException("Table configuration not found for table: " + tableName);
        }

        AuditEntryEntity auditEntry = auditEntryMapper.toEntry(auditEntryDto);
        auditEntry.setTableConfigurationId(tableConfig.getId());
        auditEntryRepository.save(auditEntry);

        AuditEntryLatestEntity newAuditEntryLatest = auditEntryLatestMapper.toEntity(auditEntryDto);
        newAuditEntryLatest.setTableConfigurationId(tableConfig.getId());
        AuditEntryLatestEntity existingLatest = auditEntryLatestRepository.findByRecordId(auditEntry.getRecordId());


        if (existingLatest == null) {
            auditEntryLatestRepository.save(newAuditEntryLatest);
        } else {
            existingLatest.setTableConfigurationId(newAuditEntryLatest.getTableConfigurationId());
            auditEntryLatestMapper.toUpdate(newAuditEntryLatest, existingLatest);
            auditEntryLatestRepository.save(existingLatest);
        }
    }
}

package org.example.auditservice.repository;

import org.example.auditservice.entity.AuditEntryLatestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEntryLatestRepository extends JpaRepository<AuditEntryLatestEntity,String> {
    AuditEntryLatestEntity findByRecordId(String recordId);
}

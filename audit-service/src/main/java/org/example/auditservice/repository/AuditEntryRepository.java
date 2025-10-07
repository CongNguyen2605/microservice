package org.example.auditservice.repository;

import org.example.auditservice.entity.AuditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditEntryRepository extends JpaRepository<AuditEntryEntity,String> {
    Optional<AuditEntryEntity> findByTableConfigurationId(String id);
}

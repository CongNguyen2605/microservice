package org.example.auditservice.repository;

import org.example.auditservice.entity.TableConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableConfigurationRepository extends JpaRepository<TableConfigurationEntity, String> {
    Optional<TableConfigurationEntity> findByTableNameAndSchemaName(String database, String tableName);

    Optional<TableConfigurationEntity> findByDatabaseNameAndSchemaNameAndTableName(String database, String schema, String tableName);
}

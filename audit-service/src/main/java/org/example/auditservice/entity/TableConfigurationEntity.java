package org.example.auditservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "table_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    private String databaseName;

    private String schemaName;

    private String tableName;

    private boolean auditEnabled;
    @Column(columnDefinition = "TEXT")
    private String excludedFields;
    @Column(columnDefinition = "TEXT")
    private String  sensitiveFields ;

    private int retentionDays;

    private boolean archiveEnabled;

    private String archiveLocation;

    private String debeziumConnectorName;
}
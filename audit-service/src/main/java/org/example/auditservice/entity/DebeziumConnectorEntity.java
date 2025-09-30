package org.example.auditservice.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.auditservice.entity.object_value.TableIdentifier;
import org.example.auditservice.entity.converter.ConnectorStatusConverter;
import org.example.auditservice.entity.converter.MapToStringConverter;
import org.example.auditservice.entity.converter.TableIdentifierSetConverter;
import org.example.auditservice.enums.ConnectorStatus;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "debezium_connectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebeziumConnectorEntity {

    @Id
    private String id;

    private String connectorName;

    private String databaseType;

    @Convert(converter = MapToStringConverter.class)
    private Map<String, String> baseConfig;

    @Convert(converter = TableIdentifierSetConverter.class)
    private Set<TableIdentifier> tables;

    @Convert(converter = ConnectorStatusConverter.class)
    private ConnectorStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private String errorMessage;
}
package org.example.auditservice.dto.debezium;

import lombok.Data;
import org.example.auditservice.entity.object_value.TableIdentifier;
import org.example.auditservice.enums.ConnectorStatus;

import java.util.Map;
import java.util.Set;

@Data
public class DebeziumDto {
    private String connectorName;
    private String databaseType;
    private Map<String, String> baseConfig;
    private Set<TableIdentifier> tables;
    private ConnectorStatus status;
    private String errorMessage;
}

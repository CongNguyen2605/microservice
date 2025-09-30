package org.example.auditservice.entity.object_value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableIdentifier {
    private String database;
    private String schema;
    private String tableName;

    public String getFullyQualifiedName() {
        return database + "." + schema + "." + tableName;
    }
}

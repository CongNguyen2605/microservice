package org.example.auditservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.auditservice.entity.object_value.FieldChange;
import org.example.auditservice.entity.converter.ChangeTypeConverter;
import org.example.auditservice.entity.converter.FieldChangeListConverter;
import org.example.auditservice.entity.converter.JsonMapConverter;
import org.example.auditservice.enums.ChangeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "audit_entry")
@Data
@NoArgsConstructor
public class AuditEntryEntity {

    @Id
    private String id;

    private String tableConfigurationId;

    private String recordId;

    @Convert(converter = ChangeTypeConverter.class)
    private ChangeType changeType;

    private Instant changeTimestamp;

    private Instant processedTimestamp;

    private String userId;

    private String sessionId;

    private String applicationName;

    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> beforeValues;

    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> afterValues;

    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = FieldChangeListConverter.class)
    private List<FieldChange> fieldChanges;

    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> metadata;
}
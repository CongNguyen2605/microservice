package org.example.auditservice.dto.audit_entry;


import lombok.Builder;
import lombok.Data;
import org.example.auditservice.entity.object_value.FieldChange;
import org.example.auditservice.entity.object_value.TableIdentifier;
import org.example.auditservice.enums.ChangeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
@Builder
@Data
public class AuditEntryDto {
    private TableIdentifier tableIdentifier;
    private String recordId;
    private ChangeType changeType;
    private Instant changeTimestamp;
    private Instant processedTimestamp;
    private String userId;
    private String sessionId;
    private String applicationName;
    private Map<String, Object> beforeValues;
    private Map<String, Object> afterValues;
    private List<FieldChange> fieldChanges;
    private Map<String, String> metadata;
}

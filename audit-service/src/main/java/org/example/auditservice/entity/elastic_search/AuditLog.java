package org.example.auditservice.entity.elastic_search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.util.Map;

@Data
@Document(indexName = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private String serviceName;
    private String actionType;
    private String userId;
    private Instant timestamp;

    private Map<String, Object> payload;
}

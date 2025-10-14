package org.example.auditservice.entity.elastic_search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = "audit_logs", createIndex = false)
@Setting(shards = 1, replicas = 0)
public class AuditLog {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String serviceName;

    @Field(type = FieldType.Keyword)
    private String actionType;

    @Field(type = FieldType.Keyword)
    private String userId;

    @Field(type = FieldType.Long)
    private Long timestamp;

    @Field(type = FieldType.Object)
    private Payload payload;
}
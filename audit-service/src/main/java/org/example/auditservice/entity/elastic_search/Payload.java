package org.example.auditservice.entity.elastic_search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payload {
    @Field(type = FieldType.Keyword)
    private String database;

    @Field(type = FieldType.Keyword)
    private String schema;

    @Field(type = FieldType.Long)
    private Long timestamp;

    @Field(type = FieldType.Object)
    private Map<String, Object> before;

    @Field(type = FieldType.Object)
    private Map<String, Object> after;
}
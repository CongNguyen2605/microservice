package org.example.auditservice.entity.object_value;

import lombok.Data;

@Data
public class FieldChange {
    private String fieldName;
    private Object before;
    private Object after;
}
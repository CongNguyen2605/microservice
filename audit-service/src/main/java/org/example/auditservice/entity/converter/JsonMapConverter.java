package org.example.auditservice.entity.converter;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return attribute != null ? mapper.writeValueAsString(attribute) : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? mapper.readValue(dbData, new TypeReference<Map<String,Object>>() {}) : new HashMap<>();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
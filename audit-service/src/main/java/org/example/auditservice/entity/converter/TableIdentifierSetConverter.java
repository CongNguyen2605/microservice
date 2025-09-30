package org.example.auditservice.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.auditservice.entity.object_value.TableIdentifier;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Converter
public class TableIdentifierSetConverter implements AttributeConverter<Set<TableIdentifier>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<TableIdentifier> attribute) {
        if (attribute == null) return "[]";
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<TableIdentifier> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new HashSet<>();
        try {
            return objectMapper.readValue(dbData, new TypeReference<Set<TableIdentifier>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
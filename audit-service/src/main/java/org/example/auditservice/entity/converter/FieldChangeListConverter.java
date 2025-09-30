package org.example.auditservice.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.auditservice.entity.object_value.FieldChange;

import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = true)
public class FieldChangeListConverter implements AttributeConverter<List<FieldChange>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<FieldChange> attribute) {
        try {
            return attribute != null ? mapper.writeValueAsString(attribute) : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting list to JSON", e);
        }
    }

    @Override
    public List<FieldChange> convertToEntityAttribute(String dbData) {
        try {
            return dbData != null ? mapper.readValue(dbData, new TypeReference<List<FieldChange>>() {}) : new ArrayList<>();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to list", e);
        }
    }
}

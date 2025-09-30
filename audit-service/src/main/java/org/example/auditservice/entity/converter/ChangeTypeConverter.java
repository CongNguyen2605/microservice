package org.example.auditservice.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.auditservice.enums.ChangeType;

@Converter(autoApply = true)
public class ChangeTypeConverter implements AttributeConverter<ChangeType, String> {

    @Override
    public String convertToDatabaseColumn(ChangeType attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public ChangeType convertToEntityAttribute(String dbData) {
        return dbData != null ? ChangeType.valueOf(dbData) : null;
    }
}
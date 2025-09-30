package org.example.auditservice.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.auditservice.enums.ConnectorStatus;

@Converter
public class ConnectorStatusConverter implements AttributeConverter<ConnectorStatus, String> {
    @Override
    public String convertToDatabaseColumn(ConnectorStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public ConnectorStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ConnectorStatus.valueOf(dbData);
    }
}

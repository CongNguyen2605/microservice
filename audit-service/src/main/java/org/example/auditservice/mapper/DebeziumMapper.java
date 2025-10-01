package org.example.auditservice.mapper;

import org.example.auditservice.dto.debezium.DebeziumDto;
import org.example.auditservice.entity.DebeziumConnectorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DebeziumMapper {
    DebeziumConnectorEntity toEntity(DebeziumDto debeziumDto);
}

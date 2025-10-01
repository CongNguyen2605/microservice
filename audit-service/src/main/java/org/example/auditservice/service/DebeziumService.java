package org.example.auditservice.service;

import org.example.auditservice.dto.debezium.DebeziumDto;
import org.example.auditservice.dto.idreponse.IdResponse;

public interface DebeziumService {
    IdResponse create(DebeziumDto debeziumDto);
}

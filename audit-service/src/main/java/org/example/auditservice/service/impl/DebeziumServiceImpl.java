package org.example.auditservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.auditservice.client.DebeziumFeignClient;
import org.example.auditservice.dto.debezium.DebeziumDto;
import org.example.auditservice.dto.idreponse.IdResponse;
import org.example.auditservice.entity.DebeziumConnectorEntity;
import org.example.auditservice.entity.TableConfigurationEntity;
import org.example.auditservice.entity.object_value.TableIdentifier;
import org.example.auditservice.mapper.DebeziumMapper;
import org.example.auditservice.repository.DebeziumRepository;
import org.example.auditservice.repository.TableConfigurationRepository;
import org.example.auditservice.service.DebeziumService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DebeziumServiceImpl implements DebeziumService {
    private final DebeziumRepository debeziumRepository;
    private final DebeziumMapper debeziumMapper;
    private final DebeziumFeignClient debeziumFeignClient;
    private final TableConfigurationRepository tableConfigurationRepository;
    @Override
    public IdResponse create(DebeziumDto debeziumDto) {
        DebeziumConnectorEntity debeziumConnectorEntity = debeziumMapper.toEntity(debeziumDto);

        debeziumConnectorEntity.setCreatedAt(Instant.now());
        debeziumRepository.save(debeziumConnectorEntity);
        Map<String,Object> debeziumConfig = new HashMap<>();
        debeziumConfig.put("name", debeziumDto.getConnectorName());
        debeziumConfig.put("config", debeziumDto.getBaseConfig());
        debeziumFeignClient.createConnector(debeziumConfig);
        List<TableConfigurationEntity> entities = debeziumDto.getTables()
                .stream()
                .map(table -> {
                    TableConfigurationEntity entity = new TableConfigurationEntity();
                    entity.setDebeziumConnectorName(debeziumDto.getConnectorName());
                    entity.setDatabaseName(table.getDatabase());
                    entity.setSchemaName(table.getSchema());
                    entity.setTableName(table.getTableName());
                    return entity;
                })
                .toList();

        tableConfigurationRepository.saveAll(entities);
        return IdResponse.builder()
                .id(debeziumConnectorEntity.getId())
                .build();
    }
}

package org.example.auditservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.example.auditservice.dto.debezium.DebeziumDto;
import org.example.auditservice.dto.idreponse.IdResponse;
import org.example.auditservice.service.DebeziumService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/debezium")
@RequiredArgsConstructor
public class DebeziumController {

    private final DebeziumService debeziumService;

    @PostMapping
    @Operation(summary = "Create Debezium Connector")
    @RequestBody(
            required = true,
            content = @Content(
                    schema = @Schema(implementation = DebeziumDto.class),
                    examples = {
                            @ExampleObject(
                                    value = """
                                            {
                                              "connectorName": "inventory-connector",
                                              "databaseType": "postgresql",
                                              "baseConfig": {
                                                "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
                                                "tasks.max": "1",
                                                "database.hostname": "host.docker.internal",
                                                "database.port": "5432",
                                                "database.user": "postgres",
                                                "database.password": "azEryMn01EPBxHczti6gEnIB3cSCIZ2024",
                                                "database.dbname": "customer-service",
                                                "database.server.name": "localhost-dbserver",
                                                "table.include.list": "public.customer",
                                                "plugin.name": "pgoutput",
                                                "slot.name": "debezium_localhost_slot",
                                                "publication.name": "dbz_localhost_publication",
                                                "snapshot.mode": "initial",
                                                "topic.prefix": "localhost-dbserver"
                                              },
                                              "tables": [
                                                   {"database":"customer-service",
                                                    "schema": "public",
                                                    "tableName": "customer"
                                                    }
                                              ],
                                              "status": "ACTIVE",
                                              "errorMessage": null
                                            }
                                            """
                            )
                    }
            )
    )
    public IdResponse create(@org.springframework.web.bind.annotation.RequestBody DebeziumDto debeziumDto) {
        return debeziumService.create(debeziumDto);
    }
}


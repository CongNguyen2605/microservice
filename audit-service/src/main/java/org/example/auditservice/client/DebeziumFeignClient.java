package org.example.auditservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "debeziumClient",
        url = "${debezium.url}"
)
public interface DebeziumFeignClient {

    @GetMapping("/connectors")
    List<String> getConnectors();

    @GetMapping("/connectors/{name}")
    Map<String, Object> getConnector(@PathVariable("name") String name);

    @GetMapping("/connectors/{name}/status")
    Map<String, Object> getConnectorStatus(@PathVariable("name") String name);

    @PostMapping("/connectors")
    Map<String, Object> createConnector(@RequestBody Map<String, Object> request);

    @PutMapping("/connectors/{name}/config")
    Map<String, Object> updateConnectorConfig(
            @PathVariable("name") String name,
            @RequestBody Map<String, String> config);

    @DeleteMapping("/connectors/{name}")
    void deleteConnector(@PathVariable("name") String name);
}

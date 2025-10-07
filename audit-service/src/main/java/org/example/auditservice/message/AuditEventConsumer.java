package org.example.auditservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.auditservice.dto.audit_entry.AuditEntryDto;
import org.example.auditservice.entity.object_value.TableIdentifier;
import org.example.auditservice.enums.ChangeType;
import org.example.auditservice.service.AuditEntryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditEntryService auditEntryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topicPattern = "localhost-dbserver\\..*"
    )
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) throws JsonProcessingException {
        if (records == null || records.isEmpty()) {
            return;
        }

        for (ConsumerRecord<String, String> record : records) {
            try {
                JsonNode jsonNode = objectMapper.readTree(record.value());

                JsonNode payload = jsonNode.path("payload");
                JsonNode source = payload.path("source");

                String schema = source.path("schema").asText("unknown");
                String database = source.path("db").asText("unknown");
                String tableName = source.path("table").asText("unknown");

                String op = payload.path("op").asText("r");
                ChangeType changeType = mapChangeType(op);

                Map<String, Object> before = payload.hasNonNull("before")
                        ? objectMapper.convertValue(payload.get("before"), new TypeReference<>() {})
                        : null;

                Map<String, Object> after = payload.hasNonNull("after")
                        ? objectMapper.convertValue(payload.get("after"), new TypeReference<>() {})
                        : null;

                String recordId = extractRecordId(record, before, after);

                TableIdentifier tableIdentifier = new TableIdentifier(database, schema, tableName);

                AuditEntryDto auditEntryDto = AuditEntryDto.builder()
                        .tableIdentifier(tableIdentifier)
                        .recordId(recordId)
                        .changeType(changeType)
                        .changeTimestamp(Instant.now())
                        .processedTimestamp(Instant.now())
                        .beforeValues(before)
                        .afterValues(after)
                        .build();

                auditEntryService.create(auditEntryDto);

            } catch (Exception e) {
                log.error("❌ Error processing record: {}", record.value(), e);
            }
        }

        ack.acknowledge();
    }

    private String extractRecordId(ConsumerRecord<String, String> record, Map<String, Object> before, Map<String, Object> after) {
        try {
            // ưu tiên lấy từ Kafka key (Debezium key là PK)
            if (record.key() != null) {
                JsonNode keyNode = objectMapper.readTree(record.key());
                JsonNode payload = keyNode.path("payload");
                if (payload.has("id")) {
                    return keyNode.get("id").asText();
                }
                return payload.toString();
            }
        } catch (Exception ignored) {}

        if (after != null && after.containsKey("id")) {
            return after.get("id").toString();
        }
        if (before != null && before.containsKey("id")) {
            return before.get("id").toString();
        }
        return UUID.randomUUID().toString(); // fallback nếu không có id
    }

    private ChangeType mapChangeType(String op) {
        return switch (op) {
            case "c" -> ChangeType.INSERT;
            case "u" -> ChangeType.UPDATE;
            case "d" -> ChangeType.DELETE;
            default -> ChangeType.UNKNOWN;
        };
    }
}

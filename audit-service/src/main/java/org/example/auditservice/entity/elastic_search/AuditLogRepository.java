package org.example.auditservice.entity.elastic_search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends ElasticsearchRepository<AuditLog, String> {
}
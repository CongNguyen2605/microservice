package org.example.auditservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.auditservice.dto.audit_aggregate.AuditAggregateDto;
import org.example.auditservice.entity.elastic_search.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/audit-aggregate")
public class AuditAggregateController {
    private final AuditLogService auditLogService;

    @GetMapping("/list")
    public Page<AuditAggregateDto> list(Pageable pageable) {
        return auditLogService.findAll(pageable);
    }

}

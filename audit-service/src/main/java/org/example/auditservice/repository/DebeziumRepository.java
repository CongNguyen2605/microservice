package org.example.auditservice.repository;

import org.example.auditservice.entity.DebeziumConnectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DebeziumRepository extends JpaRepository<DebeziumConnectorEntity,String> {
}

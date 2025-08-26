package org.example.identityservice.repository;

import org.example.identityservice.entity.RolePermissionMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionMapEntity, Long> {
    List<RolePermissionMapEntity> findAllByRoleId(Long id);
}

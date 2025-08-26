package org.example.identityservice.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RoleDto {
    private Long id;
    private String name;
    private Set<Long> permissionIds;
}

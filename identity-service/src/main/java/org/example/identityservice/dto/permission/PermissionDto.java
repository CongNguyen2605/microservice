package org.example.identityservice.dto.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PermissionDto {
    private Long id;
    private String name;
}

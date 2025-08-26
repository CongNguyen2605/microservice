package org.example.productservice.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Long roleId;
}

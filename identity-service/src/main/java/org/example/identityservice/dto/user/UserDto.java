package org.example.identityservice.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.identityservice.entity.UserEntity;
import org.example.identityservice.validator.Unique;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserDto {
    private Long id;
    @NotNull
    @Unique(entity = UserEntity.class, fieldName = "username", message = "tên đã tồn tại")
    private String username;
    @NotNull
    private String password;
    private Long roleId;
}

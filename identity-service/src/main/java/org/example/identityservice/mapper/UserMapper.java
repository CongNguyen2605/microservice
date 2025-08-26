package org.example.identityservice.mapper;

import org.example.identityservice.dto.user.UserDto;
import org.example.identityservice.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    UserEntity toEntity(UserDto user);

    @Mapping(target = "roleId", ignore = true)
    UserDto toDto(UserEntity userEntity);
}

package org.example.identityservice.service;

import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.idreponse.IdResponse;
import org.example.identityservice.dto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ApiResponse<IdResponse> create(UserDto user);

    Page<UserDto> list(Pageable pageable);

    UserDto detail(Long id);

    UserDto findUser(String username);
}

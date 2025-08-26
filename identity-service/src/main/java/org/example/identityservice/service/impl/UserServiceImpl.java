package org.example.identityservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.idreponse.IdResponse;
import org.example.identityservice.dto.user.UserDto;
import org.example.identityservice.entity.RoleEntity;
import org.example.identityservice.entity.UserEntity;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.mapper.UserMapper;
import org.example.identityservice.repository.RoleRepository;
import org.example.identityservice.repository.UserRepository;
import org.example.identityservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public ApiResponse<IdResponse> create(UserDto user) {
        UserEntity userEntity = userMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        RoleEntity roleEntity = roleRepository.findById(user.getRoleId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_EXISTS));
        userEntity.setRole(roleEntity);
        userRepository.save(userEntity);
        return new ApiResponse<>(IdResponse.builder()
                .id(userEntity.getId())
                .build());
    }

    @Override
    public Page<UserDto> list(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(userEntity -> {
            UserDto userDto = userMapper.toDto(userEntity);
            userDto.setPassword(null);
            return userDto;
        });
    }

    @Override
    public UserDto detail(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto findUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDto(userEntity);
    }
}

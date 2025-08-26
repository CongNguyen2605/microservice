package org.example.identityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.idreponse.IdResponse;
import org.example.identityservice.dto.user.UserDto;
import org.example.identityservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    ApiResponse<IdResponse> create(@Valid @RequestBody UserDto user) {
        return userService.create(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/list")
    Page<UserDto> list(Pageable pageable) {
        return userService.list(pageable);
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    @GetMapping()
    UserDto detail(@RequestParam Long id) {
        return userService.detail(id);
    }

    @GetMapping("/id")
    UserDto findUser(@RequestParam String username) {
        return userService.findUser(username);
    }
}

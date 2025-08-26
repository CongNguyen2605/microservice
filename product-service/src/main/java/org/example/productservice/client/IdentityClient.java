package org.example.productservice.client;

import org.example.productservice.dto.introspect.IntrospectRequest;
import org.example.productservice.dto.introspect.IntrospectResponse;
import org.example.productservice.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "identity-service")
public interface IdentityClient {
    @PostMapping("/api/v1/authentication/introspect")
    IntrospectResponse introspect(@RequestBody IntrospectRequest request);

    @GetMapping("api/v1/user/id")
    UserDto findUser(@RequestParam("username") String username);


}

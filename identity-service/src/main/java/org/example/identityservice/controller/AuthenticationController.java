package org.example.identityservice.controller;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.authentication.AuthenticationRequest;
import org.example.identityservice.dto.authentication.AuthenticationResponse;
import org.example.identityservice.dto.introspect.IntrospectRequest;
import org.example.identityservice.dto.introspect.IntrospectResponse;
import org.example.identityservice.dto.invalidatedtoken.InvalidatedTokenDto;
import org.example.identityservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        return ResponseEntity.ok(authenticationService.introspect(introspectRequest));
    }

    @PostMapping("/logout")
    public void logout(@RequestBody InvalidatedTokenDto invalidatedToken) throws ParseException, JOSEException {
        authenticationService.logout(invalidatedToken);
    }
}

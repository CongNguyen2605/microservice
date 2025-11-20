package org.example.identityservice.service;

import com.nimbusds.jose.JOSEException;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.authentication.AuthenticationRequest;
import org.example.identityservice.dto.authentication.AuthenticationResponse;
import org.example.identityservice.dto.authentication.RefreshTokenRequest;
import org.example.identityservice.dto.introspect.IntrospectRequest;
import org.example.identityservice.dto.introspect.IntrospectResponse;
import org.example.identityservice.dto.invalidatedtoken.InvalidatedTokenDto;
import org.example.identityservice.entity.UserEntity;

import java.text.ParseException;

public interface AuthenticationService {
    ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest);

    String generateToken(UserEntity user);

    ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) throws JOSEException, ParseException;

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;

    void logout(InvalidatedTokenDto invalidatedToken) throws ParseException, JOSEException;
}

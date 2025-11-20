package org.example.identityservice.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.example.identityservice.dto.ApiResponse;
import org.example.identityservice.dto.authentication.AuthenticationRequest;
import org.example.identityservice.dto.authentication.AuthenticationResponse;
import org.example.identityservice.dto.authentication.RefreshTokenRequest;
import org.example.identityservice.dto.introspect.IntrospectRequest;
import org.example.identityservice.dto.introspect.IntrospectResponse;
import org.example.identityservice.dto.invalidatedtoken.InvalidatedTokenDto;
import org.example.identityservice.entity.PermissionEntity;
import org.example.identityservice.entity.RoleEntity;
import org.example.identityservice.entity.RolePermissionMapEntity;
import org.example.identityservice.entity.UserEntity;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.repository.PermissionRepository;
import org.example.identityservice.repository.RolePermissionRepository;
import org.example.identityservice.repository.RoleRepository;
import org.example.identityservice.repository.UserRepository;
import org.example.identityservice.service.AuthenticationService;
import org.example.identityservice.service.BaseRedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final BaseRedisService baseRedisService;
    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.accessTokenExpiration:3600}")
    private long accessTokenExpiration;
    @NonFinal
    @Value("${jwt.refreshTokenExpiration:604800}")
    private long refreshTokenExpiration;

    @Override
    public ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        var username = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_EXISTS));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), username.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        return new ApiResponse<>(buildAuthenticationResponse(username));
    }

    @Override
    public String generateToken(UserEntity user) {
        return buildToken(user, accessTokenExpiration, "access");
    }

    @Override
    public ApiResponse<AuthenticationResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) throws JOSEException, ParseException {
        if (refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().isBlank()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        SignedJWT refreshSignedJwt = verifyToken(refreshTokenRequest.getRefreshToken(), "refresh");
        String userId = refreshSignedJwt.getJWTClaimsSet().getSubject();
        UserEntity user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        saveToBlacklist(refreshSignedJwt);

        return new ApiResponse<>(buildAuthenticationResponse(user));
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        var token = introspectRequest.getToken();

        try {
            verifyToken(token, null);
        } catch (AppException e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }
        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    @Override
    public void logout(InvalidatedTokenDto invalidatedToken) throws ParseException, JOSEException {
        if (invalidatedToken.getToken() != null) {
            var signedAccess = verifyToken(invalidatedToken.getToken(), null);
            saveToBlacklist(signedAccess);
        }
        if (invalidatedToken.getRefreshToken() != null) {
            var signedRefresh = verifyToken(invalidatedToken.getRefreshToken(), "refresh");
            saveToBlacklist(signedRefresh);
        }
    }

    private AuthenticationResponse buildAuthenticationResponse(UserEntity user) {
        String accessToken = buildToken(user, accessTokenExpiration, "access");
        String refreshToken = buildToken(user, refreshTokenExpiration, "refresh");
        Instant now = Instant.now();
        return AuthenticationResponse.builder()
                .token(accessToken)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresAt(now.plusSeconds(accessTokenExpiration).toEpochMilli())
                .refreshTokenExpiresAt(now.plusSeconds(refreshTokenExpiration).toEpochMilli())
                .authenticated(true)
                .build();
    }

    private String buildToken(UserEntity user, long expiryInSeconds, String tokenType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plusSeconds(expiryInSeconds).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .issuer("identity-service")
                .claim("token_type", tokenType)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(Base64.getDecoder().decode(SIGNER_KEY)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.valueOf(String.valueOf(e)));
        }
    }

    private List<String> buildScope(UserEntity user) {
        List<String> scopeJoiner = new ArrayList<>();

        RoleEntity roleEntity = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_EXISTS));

        List<RolePermissionMapEntity> rolePermissionMapEntities =
                rolePermissionRepository.findAllByRoleId(user.getRole().getId());

        Set<Long> permissionIds = rolePermissionMapEntities.stream()
                .map(rpm -> rpm.getPermission().getId())
                .collect(Collectors.toSet());

        List<PermissionEntity> permissionEntities = permissionRepository.findByIdIn(permissionIds);

        if (user.getRole() != null) {
            scopeJoiner.add(roleEntity.getName());
            permissionEntities.forEach(permission -> scopeJoiner.add(permission.getName()));
        }

        return scopeJoiner;
    }

    private SignedJWT verifyToken(String token, String expectedTokenType) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(Base64.getDecoder().decode(SIGNER_KEY));
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (expectedTokenType != null) {
            Object claimVal = signedJWT.getJWTClaimsSet().getClaim("token_type");
            if (claimVal == null || !expectedTokenType.equals(claimVal.toString())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        if (baseRedisService.get(jti) != null && jti != null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return signedJWT;
    }

    private void saveToBlacklist(SignedJWT signedJWT) throws ParseException {
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        long ttlSeconds = Math.max(0, (expiration.getTime() - Instant.now().toEpochMilli()) / 1000);
        baseRedisService.set(jti, "revoked");
        baseRedisService.setTimeToLive(jti, ttlSeconds);
    }

}

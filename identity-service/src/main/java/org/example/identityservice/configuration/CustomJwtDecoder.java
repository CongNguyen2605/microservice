package org.example.identityservice.configuration;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.service.AuthenticationService;
import org.example.identityservice.service.BaseRedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final BaseRedisService baseRedisService;
    private final AuthenticationService authenticationService;
    @Value("${jwt.signerKey}")
    private String signerKey;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            if (jti != null && baseRedisService.get(jti) != null) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}

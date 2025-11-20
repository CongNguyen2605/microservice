package org.example.gatewayservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${jwt.signerKey}")
    private String signerKey;

    public Long extractUserId(String token) {
        return Optional.ofNullable(parseClaims(token))
                .map(claims -> Long.valueOf(claims.getSubject()))
                .orElse(null);
    }

    public String extractUsername(String token) {
        return Optional.ofNullable(parseClaims(token))
                .map(claims -> claims.get("username", String.class))
                .orElse(null);
    }

    public List<String> extractScopes(String token) {
        return Optional.ofNullable(parseClaims(token))
                .map(claims -> {
                    Object scope = claims.get("scope");
                    if (scope instanceof List<?> scopes) {
                        return scopes.stream().map(Object::toString).toList();
                    }
                    return Collections.<String>emptyList();
                })
                .orElse(Collections.emptyList());
    }

    private Claims parseClaims(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(signerKey);
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

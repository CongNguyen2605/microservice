package org.example.productservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String signerKey;

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signerKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("sub", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
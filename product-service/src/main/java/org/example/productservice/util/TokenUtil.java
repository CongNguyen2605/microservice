package org.example.productservice.util;

import lombok.RequiredArgsConstructor;
import org.example.productservice.client.IdentityClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Component
public class TokenUtil {
    private final IdentityClient identityClient;
    private final JwtUtil jwtUtil;

    public Long getUserIdFromToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        String authHeader = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        String token = authHeader.substring(7);
        Long id = Long.valueOf(jwtUtil.extractId(token));
        return id;
    }
}
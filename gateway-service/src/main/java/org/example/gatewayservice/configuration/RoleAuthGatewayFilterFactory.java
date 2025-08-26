package org.example.gatewayservice.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RoleAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleAuthGatewayFilterFactory.Config> {
    @Value("${jwt.signerKey}")
    private String signerKey;

    public RoleAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("requiredScope");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            List<String> authHeaders = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);

            if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeaders.get(0).substring(7);

            Claims claims;
            try {
                claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(signerKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (Exception e) {
                return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }

            List<String> scopes = (List<String>) claims.get("scope");
            System.out.println("Checking scope: " + config.getRequiredScope());
            System.out.println("Token scopes: " + scopes);

            if (scopes == null || scopes.stream().noneMatch(s -> s.equals(config.getRequiredScope()))) {
                return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        private String requiredScope;

        public Config() {
        }

        public Config(String requiredScope) {
            this.requiredScope = requiredScope;
        }

        public String getRequiredScope() {
            return requiredScope;
        }

        public void setRequiredScope(String requiredScope) {
            this.requiredScope = requiredScope;
        }
    }
}



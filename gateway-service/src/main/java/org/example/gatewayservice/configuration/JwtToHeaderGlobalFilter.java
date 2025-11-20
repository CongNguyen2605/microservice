package org.example.gatewayservice.configuration;

import org.example.gatewayservice.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtToHeaderGlobalFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public JwtToHeaderGlobalFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtService.extractUserId(token);
            String username = jwtService.extractUsername(token);
            var scopes = jwtService.extractScopes(token);

            if (userId != null) {
                ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                        .header("X-User-Id", String.valueOf(userId));
                if (username != null) {
                    requestBuilder.header("X-Username", username);
                }
                if (scopes != null && !scopes.isEmpty()) {
                    requestBuilder.header("X-Roles", String.join(",", scopes));
                }
                ServerHttpRequest mutatedRequest = requestBuilder.build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

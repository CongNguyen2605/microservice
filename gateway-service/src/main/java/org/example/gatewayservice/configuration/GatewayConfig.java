package org.example.gatewayservice.configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("identity-service", r -> r.path("/api/v1/authentication/**", "/api/v1/user/**")
                        .uri("lb://identity-service"))
                .route("product-service", r -> r.path("/api/v1/product/**", "/api/v1/cart/**")
                        .uri("lb://product-service"))
                .route("audit-service", r -> r.path("/api/v1/debezium/**")
                        .uri("lb://audit-service"))
                .build();
    }

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange.mutate().request(builder -> builder.headers(httpHeaders -> {
                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    httpHeaders.set(HttpHeaders.AUTHORIZATION, exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
                }
            })).build());
        };
    }
}

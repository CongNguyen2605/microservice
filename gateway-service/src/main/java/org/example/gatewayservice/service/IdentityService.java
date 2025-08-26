package org.example.gatewayservice.service;

import lombok.RequiredArgsConstructor;
import org.example.gatewayservice.dto.introspect.IntrospectRequest;
import org.example.gatewayservice.dto.introspect.IntrospectResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class IdentityService {

    private final WebClient.Builder webClientBuilder;

    public Mono<IntrospectResponse> introspect(String token) {
        IntrospectRequest request = new IntrospectRequest();
        request.setToken(token);

        return webClientBuilder
                .baseUrl("lb://identity-service")
                .build()
                .post()
                .uri("/api/v1/authentication/introspect")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(IntrospectResponse.class)
                .onErrorResume(e -> {
                    IntrospectResponse fallback = new IntrospectResponse();
                    fallback.setValid(false);
                    return Mono.just(fallback);
                });
    }
}

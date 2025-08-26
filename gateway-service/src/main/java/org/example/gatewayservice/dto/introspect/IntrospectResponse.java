package org.example.gatewayservice.dto.introspect;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectResponse {
    private boolean valid;
}


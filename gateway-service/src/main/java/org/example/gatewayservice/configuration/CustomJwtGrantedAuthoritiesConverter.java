package org.example.gatewayservice.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> scopes = jwt.getClaimAsStringList("scope");
        if (scopes == null) {
            return List.of();
        }
        return scopes.stream()
                .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope.toUpperCase()))
                .collect(Collectors.toList());
    }
}
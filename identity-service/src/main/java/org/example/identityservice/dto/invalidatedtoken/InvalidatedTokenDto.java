package org.example.identityservice.dto.invalidatedtoken;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class InvalidatedTokenDto {
    private String token;
}

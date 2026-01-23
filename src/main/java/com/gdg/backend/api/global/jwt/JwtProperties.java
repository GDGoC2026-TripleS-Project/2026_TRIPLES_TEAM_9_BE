package com.gdg.backend.api.global.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

    @NotNull
    private String secret;

    @NotNull(message = "ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS is required")
    private Long accessTokenValidity;

    @NotNull(message = "REFRESH_TOKEN_VALIDITY_IN_MILLISECONDS is required")
    private Long refreshTokenValidity;

    @NotNull(message = "SIGNUP_TOKEN_VALIDITY_IN_MILLISECONDS is required")
    private Long signupTokenValidity;


}


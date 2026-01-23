package com.gdg.backend.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthLoginRequestDto {
    @NotBlank
    private String authToken;
}

package com.gdg.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthLoginRequestDto {
    @NotBlank
    private String idToken;
}

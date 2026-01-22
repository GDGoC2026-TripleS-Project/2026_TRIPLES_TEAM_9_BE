package com.gdg.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthOnboardingRequestDto {
    @NotBlank
    private String idToken;
    @NotBlank
    private String nickname;
}

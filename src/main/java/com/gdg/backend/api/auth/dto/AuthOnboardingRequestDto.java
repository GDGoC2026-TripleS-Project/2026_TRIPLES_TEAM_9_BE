package com.gdg.backend.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthOnboardingRequestDto {
    @NotBlank
    private String authToken;
    @NotBlank
    private String nickname;
    //회원가입 시 추가 정보는 여기다가 추ㅏㄱ
}

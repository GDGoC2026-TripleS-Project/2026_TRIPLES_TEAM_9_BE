package com.gdg.backend.auth.controller;

import com.gdg.backend.auth.dto.AuthLoginRequestDto;
import com.gdg.backend.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.auth.service.AuthService;
import com.gdg.backend.global.code.SuccessCode;
import com.gdg.backend.global.response.ApiResponse;
import com.gdg.backend.user.dto.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OIDC 로그인 및 회원가입")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "OIDC 로그인", description = "기존 유저 로그인 입니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(
            @RequestBody @Valid AuthLoginRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.LOGIN_SUCCESS,
                authService.login(request)
        );
    }

    @Operation(summary = "신규 회원가입", description = "신규 유저들은 닉네임과 유형을 선택합니다.")
    @PostMapping(
            value = "/onboarding",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<TokenResponseDto>> onboarding(
            @RequestPart("data") @Valid AuthOnboardingRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.USER_CREATED,
                authService.onboarding(request)
        );
    }


}

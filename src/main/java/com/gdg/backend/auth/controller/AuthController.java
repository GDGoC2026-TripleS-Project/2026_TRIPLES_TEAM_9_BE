package com.gdg.backend.auth.controller;

import com.gdg.backend.auth.dto.AuthIssueResult;
import com.gdg.backend.auth.dto.AuthLoginRequestDto;
import com.gdg.backend.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.auth.dto.AuthTokenResponseDto;
import com.gdg.backend.auth.service.AuthService;
import com.gdg.backend.global.code.SuccessCode;
import com.gdg.backend.global.cookie.CookieUtil;
import com.gdg.backend.global.jwt.TokenProvider;
import com.gdg.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
    private final CookieUtil cookieUtil;
    private final TokenProvider tokenProvider;

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String COOKIE_PATH = "/";

    private final boolean cookieSecure = false;
    private final String cookieSameSite = "Lax";

    @Operation(summary = "OIDC 로그인", description = "기존 유저 로그인 입니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> login(
            @RequestBody @Valid AuthLoginRequestDto request,
            HttpServletResponse response
    ) {
        AuthIssueResult result = authService.login(request);

        setRefreshCookie(response, result.tokens().refreshToken());

        return ApiResponse.success(
                SuccessCode.LOGIN_SUCCESS,
                toResponseDto(result)
        );
    }

    @Operation(summary = "신규 회원가입", description = "신규 유저들은 닉네임과 유형을 선택합니다.")
    @PostMapping(
            value = "/onboarding",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> onboarding(
            @RequestPart("data") @Valid AuthOnboardingRequestDto request,
            HttpServletResponse response
    ) {
        AuthIssueResult result = authService.onboarding(request);

        // refreshToken은 쿠키로만 세팅
        setRefreshCookie(response, result.tokens().refreshToken());

        return ApiResponse.success(
                SuccessCode.USER_CREATED,
                toResponseDto(result)
        );
    }

    @Operation(summary = "Access Token 재발급", description = "httpOnly refreshToken 쿠키로 accessToken을 재발급합니다. (refresh 로테이션 가능)")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> refresh(
            @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        AuthIssueResult result = authService.refresh(refreshToken);

        setRefreshCookie(response, result.tokens().refreshToken());

        return ApiResponse.success(
                SuccessCode.TOKEN_REFRESH_SUCCESS,
                toResponseDto(result)
        );
    }

    @Operation(summary = "로그아웃", description = "refreshToken 쿠키를 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        cookieUtil.deleteCookie(response, REFRESH_COOKIE_NAME, cookieSecure, cookieSameSite, COOKIE_PATH);
        return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS, null);
    }

    private AuthTokenResponseDto toResponseDto(AuthIssueResult result) {
        return AuthTokenResponseDto.builder()
                .accessToken(result.tokens().accessToken())
                .userId(result.userId())
                .email(result.email())
                .nickname(result.nickname())
                .oauthProvider(result.oauthProvider())
                .role(result.role())
                .newUser(result.newUser())
                .build();
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        long maxAgeSeconds = tokenProvider.getRefreshTokenValidityMillis() / 1000;

        cookieUtil.addHttpOnlyCookie(
                response,
                REFRESH_COOKIE_NAME,
                refreshToken,
                maxAgeSeconds,
                cookieSecure,
                cookieSameSite,
                COOKIE_PATH
        );
    }
}

package com.gdg.backend.api.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode {
    NO_REFRESH_TOKEN(90001, HttpStatus.BAD_REQUEST, "Refresh token이 없습니다."),
    USER_NOT_FOUND(90002, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(90003, HttpStatus.UNAUTHORIZED, "Refresh token이 유효하지 않습니다."),
    NOT_PENDING_USER(90004, HttpStatus.BAD_REQUEST, "회원가입 대상 사용자가 아닙니다."),
    TOKEN_USER_MISMATCH(90005, HttpStatus.BAD_REQUEST, "토큰 정보와 사용자 정보가 일치하지 않습니다."),
    USER_NOT_FOUND_FOR_ONBOARDING(90006, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;

    AuthErrorCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}

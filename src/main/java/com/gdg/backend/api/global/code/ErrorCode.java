package com.gdg.backend.api.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //기본값
    INVALID_REQUEST(20001, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_PARAMETER(20002, HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_FORMAT(20003, HttpStatus.BAD_REQUEST, "지원하지 않는 형식입니다."),
    FILE_TOO_LARGE(20004, HttpStatus.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."),
    TOO_MANY_REQUESTS(20005, HttpStatus.TOO_MANY_REQUESTS, "요청 횟수를 초과했습니다."),
    INVALID_TYPE(20006, HttpStatus.BAD_REQUEST, "지원하지 않는 로그인 타입입니다."),

    //oauth
    OAUTH_INVALID_REQUEST(11001, HttpStatus.BAD_REQUEST, "잘못된 OAuth 요청입니다."),
    OAUTH_PROVIDER_ERROR(11002, HttpStatus.BAD_REQUEST, "Provider에서 사용자 정보를 가져올 수 없습니다."),
    OAUTH_INVALID_CODE(11003, HttpStatus.BAD_REQUEST, "유효하지 않은 OAuth 인가 코드입니다."),

    TOKEN_EXPIRED(11010, HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다."),
    TOKEN_INVALID(11011, HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(11012, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_INVALID(11013, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    TOKEN_SIGNATURE_INVALID(11014, HttpStatus.UNAUTHORIZED, "토큰 서명 검증에 실패했습니다."),

    NO_AUTHORITY(11020, HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
    ACCESS_DENIED(11021, HttpStatus.FORBIDDEN, "해당 리소스에 접근할 수 없습니다."),

    OAUTH_ALREADY_REGISTERED(11030, HttpStatus.CONFLICT, "이미 가입된 SNS 계정입니다."),
    OAUTH_ALREADY_CONNECTED(11031, HttpStatus.CONFLICT, "이미 연결된 OAuth Provider입니다."),

    //user
    USER_NOT_FOUND(10001, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(10002, HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),
    INVALID_NICKNAME_FORMAT(10003, HttpStatus.BAD_REQUEST, "닉네임 형식이 올바르지 않습니다."),
    DUPLICATED_NICKNAME(10004, HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    USER_DISABLED(10005, HttpStatus.FORBIDDEN, "비활성화된 계정입니다."),
    USER_UNAUTHORIZED(10006, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    //server
    INTERNAL_SERVER_ERROR(90001, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(90002, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(90003, HttpStatus.BAD_GATEWAY, "외부 API 호출 중 오류가 발생했습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;

}

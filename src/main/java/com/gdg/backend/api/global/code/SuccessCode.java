package com.gdg.backend.api.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    //200
    OK(0, HttpStatus.OK, "요청이 정상적으로 처리되었습니다."),
    READ_SUCCESS(1, HttpStatus.OK, "조회가 완료되었습니다."),
    LOGIN_SUCCESS(2, HttpStatus.OK, "로그인이 완료되었습니다."),
    LOGOUT_SUCCESS(3, HttpStatus.OK, "로그아웃이 완료되었습니다."),
    PROCESS_SUCCESS(4, HttpStatus.OK, "정상적으로 처리되었습니다."),
    RECORD_LIST_SUCCESS(5,HttpStatus.OK, "학습 기록 조회가 완료되었습니다."),
    RECORD_DETAILS_SUCCESS(6, HttpStatus.OK, "학습 기록 세부 정보 조회가 완료되었습니다."),

    //201
    CREATED(100, HttpStatus.CREATED, "게시판 작성 요청이 정상적으로 처리되었습니다."),
    USER_CREATED(101, HttpStatus.CREATED, "사용자가 정상적으로 생성되었습니다."),
    ADMIN_CREATED(103, HttpStatus.CREATED, "관리자 계정이 생성되었습니다."),
    TOKEN_REFRESH_SUCCESS(104, HttpStatus.OK, "RefreshToken이 정상적으로 생성되었습니다."),
    RECORD_CREATED(105, HttpStatus.CREATED, "학습기록이 정상적으로 생성되었습니다."),

    //202
    UPDATE(200, HttpStatus.OK, "게시판 수정이 정상적으로 처리되었습니다."),
    USER_UPDATE(201, HttpStatus.OK, "사용자 수정이 정상적으로 처리되었습니다."),
    IMAGE_UPDATE(203, HttpStatus.OK, "이미지 변경이 정상적으로 처리되었습니다."),

    //203

    DELETE(300, HttpStatus.OK, "게시판 삭제가 정상적으로 처리되었습니다."),
    USER_DELETE(301, HttpStatus.OK, "탈퇴 처리가 정상적으로 처리되었습니다."),
    IMAGE_DELETE(303, HttpStatus.OK,"이미지 삭제가 정상적으로 처리되었습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;
}

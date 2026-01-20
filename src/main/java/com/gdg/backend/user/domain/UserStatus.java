package com.gdg.backend.user.domain;

public enum UserStatus {
    PENDING, //Oauth 인증자
    ACTIVE, //정상 인증
    SUSPENDING //정지, 또는 활동 중단
}

package com.gdg.backend.api.user.account.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.user.account.dto.UserInfoResponseDto;
import com.gdg.backend.api.user.account.dto.UserUpdateRequestDto;
import com.gdg.backend.api.user.account.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "Token을 이용하여 내 정보를 확인할 수 있습니다.")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        Long userId = principal.userId();
        return ApiResponse.success(SuccessCode.READ_SUCCESS,userService.getMyInfo(userId));
    }

    @Operation(summary = "정보 변경", description = "Token을 이용하여 이름, 프로필, 타입을 변경할 수 있습니다.")
    @PatchMapping(
            value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<Object>> updateMyInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserUpdateRequestDto request
    ) {
        userService.updateUser(principal.userId(), request);
        return ApiResponse.success(SuccessCode.USER_UPDATE);
    }

    @Operation(summary = "회원탈퇴", description = "탈퇴하는 api입니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Long userId = principal.userId();

        userService.deleteUser(userId);
        return ApiResponse.success(SuccessCode.USER_DELETE);
    }
}

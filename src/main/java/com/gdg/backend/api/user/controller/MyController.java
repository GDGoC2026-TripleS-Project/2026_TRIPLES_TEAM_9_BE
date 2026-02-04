package com.gdg.backend.api.user.controller;

import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.user.dto.UserProfileResponseDto;
import com.gdg.backend.api.user.dto.UserProfileUpdateRequestDto;
import com.gdg.backend.api.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getMe(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UserProfileResponseDto response = userProfileService.getMyProfile(principal.userId());
        return ApiResponse.success(SuccessCode.READ_SUCCESS, response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UserProfileUpdateRequestDto request
    ) {
        UserProfileResponseDto response =
                userProfileService.updateMyProfile(principal.userId(), request);

        return ApiResponse.success(SuccessCode.USER_UPDATE, response);
    }
}

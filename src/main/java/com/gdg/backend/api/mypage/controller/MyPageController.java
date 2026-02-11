package com.gdg.backend.api.mypage.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.mypage.dto.MindmapSummaryResponseDto;
import com.gdg.backend.api.mypage.dto.RecentActivityDto;
import com.gdg.backend.api.mypage.service.MyPageActivityService;
import com.gdg.backend.api.mypage.service.MindmapSummaryService;
import com.gdg.backend.api.user.account.dto.UserInfoResponseDto;
import com.gdg.backend.api.user.account.dto.UserUpdateRequestDto;
import com.gdg.backend.api.user.account.service.UserService;
import com.gdg.backend.api.user.profile.dto.UserProfileResponseDto;
import com.gdg.backend.api.user.profile.dto.UserProfileUpdateRequestDto;
import com.gdg.backend.api.user.profile.service.UserProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
@Tag(name = "마이페이지")
public class MyPageController {

    private final MyPageActivityService myPageActivityService;
    private final MindmapSummaryService mindmapSummaryService;
    private final UserProfileService userProfileService;
    private final UserService userService;

    @Operation(
            summary = "마이페이지 - 프로필 조회",
            description = """
                    로그인한 사용자의 마이페이지 프로필 정보를 조회합니다.
                    
                    조회 항목:
                    - 닉네임
                    - 학습 목표
                    - 학습 분야
                    - 학습 목표 상세
                    - 나의 다짐
                    """
    )
    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                userProfileService.getMyProfile(principal.userId())
        );
    }

    @Operation(
            summary = "마이페이지 - 프로필 수정",
            description = """
                    마이페이지에서 프로필 정보를 수정합니다.
                    
                    수정 가능 항목:
                    - 닉네임
                    - 학습 목표
                    - 학습 분야
                    - 학습 목표 상세
                    - 나의 다짐
                    
                    이메일은 수정할 수 없습니다.
                    """
    )
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UserProfileUpdateRequestDto request
    ) {
        return ApiResponse.success(
                SuccessCode.USER_UPDATE,
                userProfileService.updateMyProfile(principal.userId(), request)
        );
    }

    @Operation(
            summary = "마이페이지 - 최근 학습 활동 조회",
            description = """
                    로그인한 사용자의 최근 학습 활동 목록을 조회합니다.
                    
                    - 최신순으로 정렬됩니다.
                    - 카드 UI에 필요한 요약 정보만 반환합니다.
                    - 기본 조회 개수는 10개입니다.
                    """
    )
    @GetMapping("/activities/recent")
    public ResponseEntity<ApiResponse<List<RecentActivityDto>>> recentActivities(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                myPageActivityService.getRecentActivities(principal.userId(), size)
        );
    }

    @Operation(
            summary = "마이페이지 - 마인드맵 요약",
            description = """
                    마인드맵 요약 화면을 위한 키워드/기록 요약 정보를 조회합니다.
                    
                    - keywords: 상위 키워드 목록
                    - selected: 선택된 키워드 요약
                    - records: 선택 키워드에 매칭된 최근 기록
                    """
    )
    @GetMapping("/mindmap/summary")
    public ResponseEntity<ApiResponse<MindmapSummaryResponseDto>> mindmapSummary(
            @AuthenticationPrincipal UserPrincipal principal,
            @Parameter(description = "왼쪽 키워드 목록 상위 N개 (default=5, max=20)")
            @RequestParam(required = false) Integer top,
            @Parameter(description = "선택된 키워드 ID (없으면 top 1)")
            @RequestParam(required = false) Long keywordId,
            @Parameter(description = "선택된 키워드에 매칭되는 최근 기록 N개 (default=10, max=50)")
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                mindmapSummaryService.getSummary(principal.userId(), top, keywordId, size)
        );
    }

    @Operation(
            summary = "마이페이지 - 계정 정보 조회",
            description = """
                    로그인한 사용자의 계정 정보를 조회합니다.
                    
                    조회 항목:
                    - 이메일
                    - 닉네임
                    - 권한(Role)
                    - OAuth 제공자
                    """
    )
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                userService.getMyInfo(principal.userId())
        );
    }

    @Operation(
            summary = "마이페이지 - 계정 정보 수정",
            description = """
                    로그인한 사용자의 계정 정보를 수정합니다.
                    
                    수정 가능 항목:
                    - 닉네임
                    
                    이메일 및 OAuth 정보는 수정할 수 없습니다.
                    """
    )
    @PatchMapping("/account")
    public ResponseEntity<ApiResponse<Object>> updateMyInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserUpdateRequestDto request
    ) {
        userService.updateUser(principal.userId(), request);
        return ApiResponse.success(SuccessCode.USER_UPDATE);
    }

    @Operation(
            summary = "마이페이지 - 회원 탈퇴",
            description = """
                    로그인한 사용자를 탈퇴 처리합니다.
                    
                    - 사용자 계정 및 관련 데이터가 삭제됩니다.
                    - 탈퇴 후에는 회원가입을 다시 해야합니다.
                    """
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.deleteUser(principal.userId());
        return ApiResponse.success(SuccessCode.USER_DELETE);
    }
}

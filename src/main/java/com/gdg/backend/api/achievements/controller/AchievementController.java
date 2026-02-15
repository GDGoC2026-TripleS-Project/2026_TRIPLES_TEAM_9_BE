package com.gdg.backend.api.achievements.controller;

import com.gdg.backend.api.achievements.dto.AchievementResponseDto;
import com.gdg.backend.api.achievements.service.AchievementService;
import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/achievements")
@Tag(name = "업적")
public class AchievementController {

    private final AchievementService achievementService;

    @Operation(
            summary = "업적 조회",
            description = "유저의 학습 기록 기반 업적/뱃지 진행도를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<AchievementResponseDto>> getAchievements(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                SuccessCode.ACHIEVEMENT_SUCCESS,
                achievementService.getAchievements(principal.userId())
        );
    }
}

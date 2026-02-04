package com.gdg.backend.api.dashboard.controller;

import com.gdg.backend.api.dashboard.dto.DashboardRequestDto;
import com.gdg.backend.api.dashboard.dto.DashboardResponseDto;
import com.gdg.backend.api.dashboard.service.DashboardService;
import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponseDto>> getDashboard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute DashboardRequestDto req
    ) {
        Long userId = userPrincipal.userId();
        DashboardResponseDto res = dashboardService.getDashboard(userId, req);

        return ApiResponse.success(SuccessCode.READ_SUCCESS, res);
    }
}

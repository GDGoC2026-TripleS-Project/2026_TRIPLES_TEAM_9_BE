package com.gdg.backend.api.dashBoard.controller;

import com.gdg.backend.api.dashBoard.dto.DashBoardRequestDto;
import com.gdg.backend.api.dashBoard.dto.DashBoardResponseDto;
import com.gdg.backend.api.dashBoard.service.DashBoardService;
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
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashBoardResponseDto>> getDashBoard(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute DashBoardRequestDto req
    ) {
        Long userId = userPrincipal.userId();
        DashBoardResponseDto res = dashBoardService.getDashBoard(userId, req);

        return ApiResponse.success(SuccessCode.READ_SUCCESS, res);
    }
}

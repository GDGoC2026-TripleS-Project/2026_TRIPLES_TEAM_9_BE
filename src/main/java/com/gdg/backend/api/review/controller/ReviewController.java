package com.gdg.backend.api.review.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.review.dto.ReviewTodayResponseDto;
import com.gdg.backend.api.review.dto.ReviewViewedBatchRequestDto;
import com.gdg.backend.api.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<ReviewTodayResponseDto>> getTodayReview(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                SuccessCode.REVIEW_TODAY_SUCCESS,
                reviewService.getTodayReview(principal.userId())
        );
    }

    @PostMapping("/viewed/batch")
    public ResponseEntity<ApiResponse<Void>> markViewedBatch(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ReviewViewedBatchRequestDto request
    ) {
        reviewService.markViewedBatch(principal.userId(), request.getRecordIds());
        return ApiResponse.success(SuccessCode.REVIEW_VIEWED_SUCCESS, null);
    }
}

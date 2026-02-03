package com.gdg.backend.api.mindMap.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.mindMap.dto.MindMapResponseDto;
import com.gdg.backend.api.mindMap.service.MindMapService;
import com.gdg.backend.api.record.domain.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "MindMap 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/mindmap")
public class MindMapController {

    private final MindMapService mindMapService;

    @Operation(
            summary = "MindMap 조회",
            description = "키워드 공존재를 기반으로 런타임 MindMap을 생성합니다. "
                    + "예) GET /mindmap?from=2025-01-01&to=2025-01-31&category=LECTURE&topKeywords=30&minEdgeWeight=2"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<MindMapResponseDto>> getMindMap(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "30") @Min(1) int topKeywords,
            @RequestParam(defaultValue = "2") @Min(1) int minEdgeWeight
    ) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from은 to보다 늦을 수 없습니다.");
        }

        Long userId = principal.userId();

        MindMapResponseDto response = mindMapService.getMindMap(
                userId,
                from,
                to,
                category,
                topKeywords,
                minEdgeWeight
        );

        return ApiResponse.success(SuccessCode.READ_SUCCESS, response);
    }
}

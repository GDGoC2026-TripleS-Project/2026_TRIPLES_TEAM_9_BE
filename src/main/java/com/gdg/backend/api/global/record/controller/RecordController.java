package com.gdg.backend.api.global.record.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.global.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.global.record.dto.RecordListResponseDto;
import com.gdg.backend.api.global.record.service.RecordService;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateRecordResponseDto>> create(
            @Valid @RequestBody CreateRecordRequestDto req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        Long userId = userPrincipal.userId();
        CreateRecordResponseDto res = recordService.create(userId, req);

        return ApiResponse.success(SuccessCode.RECORD_CREATED, res);
    }

    @GetMapping("/read")
    public ResponseEntity<ApiResponse<Page<RecordListResponseDto>>> getRecords(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false) Category category
            ){
        return ApiResponse.success(SuccessCode.RECORD_LIST_SUCCESS,recordService.getRecords(page, category));
    }
}

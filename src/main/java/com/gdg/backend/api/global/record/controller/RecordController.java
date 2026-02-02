package com.gdg.backend.api.global.record.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.global.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.global.record.dto.RecordDetailResponseDto;
import com.gdg.backend.api.global.record.dto.RecordListResponseDto;
import com.gdg.backend.api.global.record.dto.UpdateRecordDetailRequestDto;
import com.gdg.backend.api.global.record.dto.UpdateRecordDetailResponseDto;
import com.gdg.backend.api.global.record.service.RecordService;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    //학습기록 전체/카테고리별 조회
    @GetMapping("/lists")
    public ResponseEntity<ApiResponse<Page<RecordListResponseDto>>> getMyRecords(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false) Category category
            ){
        return ApiResponse.success(SuccessCode.RECORD_LIST_SUCCESS,recordService.getMyRecords(userPrincipal.userId(), page, category));
    }

    //학습기록 세부 정보 조회
    @GetMapping("/details/{recordId}")
    public ResponseEntity<ApiResponse<RecordDetailResponseDto>> getRecordDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId
            ) {
        return ApiResponse.success(SuccessCode.RECORD_DETAILS_SUCCESS, recordService.getRecordDetails(userPrincipal.userId(), recordId));
    }

    //성공 시 수정된 값이 반환됨
    @PutMapping("/details-update/{recordId}")
    public ResponseEntity<ApiResponse<UpdateRecordDetailResponseDto>> updateRecordDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId,
            @RequestBody @Valid UpdateRecordDetailRequestDto req
    ) {
        UpdateRecordDetailResponseDto res = recordService.updateRecord(userPrincipal.userId(), recordId, req);

        return ApiResponse.success(SuccessCode.RECORD_UPDATE_SUCCESS, res);
    }

    //성공 시 data는 null 값 반환됨
    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<ApiResponse<Object>> deleteRecord(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId
    ) {
        recordService.deleteRecord(userPrincipal.userId(), recordId);

        return ApiResponse.success(SuccessCode.RECORD_DELETE);
    }
}

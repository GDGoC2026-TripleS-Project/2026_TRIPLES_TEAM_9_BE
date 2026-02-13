package com.gdg.backend.api.record.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.record.dto.RecordDetailResponseDto;
import com.gdg.backend.api.record.dto.RecordListResponseDto;
import com.gdg.backend.api.record.dto.UpdateRecordDetailRequestDto;
import com.gdg.backend.api.record.dto.UpdateRecordDetailResponseDto;
import com.gdg.backend.api.record.service.RecordService;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
@Tag(name = "학습 기록 컨트롤러")
public class RecordController {

    private final RecordService recordService;

    @Operation(
            summary = "학습 기록 생성",
            description = "학습 기록을 생성합니다."
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateRecordResponseDto>> create(
            @Valid @RequestBody CreateRecordRequestDto req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        Long userId = userPrincipal.userId();
        CreateRecordResponseDto res = recordService.create(userId, req);

        return ApiResponse.success(SuccessCode.RECORD_CREATED, res);
    }

    @Operation(
            summary = "학습 기록 목록 조회",
            description = "학습 기록을 전체 또는 카테고리별로 페이지 단위 조회합니다."
    )
    @GetMapping("/lists")
    public ResponseEntity<ApiResponse<Page<RecordListResponseDto>>> getMyRecords(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "4") @Min(1) int size,
            @RequestParam(required = false) String category
    ) {
        Category parsedCategory = null;
        if (category != null && !category.isBlank()) {
            parsedCategory = Category.valueOf(category.toUpperCase());
        }

        int zeroBasedPage = page - 1;

        return ApiResponse.success(
                SuccessCode.RECORD_LIST_SUCCESS,
                recordService.getMyRecords(userPrincipal.userId(), zeroBasedPage, size, parsedCategory)
        );
    }


    @Operation(
            summary = "학습 기록 상세 조회",
            description = "지정한 학습 기록의 상세 정보를 조회합니다."
    )
    @GetMapping("/details/{recordId}")
    public ResponseEntity<ApiResponse<RecordDetailResponseDto>> getRecordDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId
            ) {
        return ApiResponse.success(SuccessCode.RECORD_DETAILS_SUCCESS, recordService.getRecordDetails(userPrincipal.userId(), recordId));
    }

    @Operation(
            summary = "학습 기록 수정",
            description = "지정한 학습 기록의 내용을 수정합니다."
    )
    @PatchMapping("/update/{recordId}")
    public ResponseEntity<ApiResponse<UpdateRecordDetailResponseDto>> updateRecordDetails(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId,
            @RequestBody @Valid UpdateRecordDetailRequestDto req
    ) {
        UpdateRecordDetailResponseDto res = recordService.updateRecord(userPrincipal.userId(), recordId, req);

        return ApiResponse.success(SuccessCode.RECORD_UPDATE, res);
    }

    @Operation(
            summary = "학습 기록 삭제",
            description = "지정한 학습 기록을 삭제합니다."
    )
    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<ApiResponse<Object>> deleteRecord(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long recordId
    ) {
        recordService.deleteRecord(userPrincipal.userId(), recordId);

        return ApiResponse.success(SuccessCode.RECORD_DELETE);
    }
}

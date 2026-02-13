package com.gdg.backend.api.goal.controller;

import com.gdg.backend.api.global.code.SuccessCode;
import com.gdg.backend.api.global.response.ApiResponse;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.goal.dto.CreateGoalRequestDto;
import com.gdg.backend.api.goal.dto.CreateGoalResponseDto;
import com.gdg.backend.api.goal.dto.CreateTaskRequestDto;
import com.gdg.backend.api.goal.dto.CreateTaskResponseDto;
import com.gdg.backend.api.goal.dto.GoalListResponseDto;
import com.gdg.backend.api.goal.dto.TaskResponseDto;
import com.gdg.backend.api.goal.service.GoalService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
@Tag(name = "목표 관리 컨트롤러")
public class GoalController {

    private final GoalService goalService;

    //목표
    @Operation(
            summary = "목표 생성",
            description = "목표를 생성합니다."
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateGoalResponseDto>> createGoal(
            @Valid @RequestBody CreateGoalRequestDto req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CreateGoalResponseDto res = goalService.createGoal(userPrincipal.userId(), req);

        return ApiResponse.success(SuccessCode.GOAL_CREATED, res);
    }

    @Operation(
            summary = "목표 목록 조회",
            description = "유저의 목표 목록을 페이지 단위로 조회합니다."
    )
    @GetMapping("/lists")
    public ResponseEntity<ApiResponse<Page<GoalListResponseDto>>> getGoals(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") @Min(0) int page
    ){
        return ApiResponse.success(SuccessCode.GOAL_LIST_SUCCESS,goalService.getGoals(userPrincipal.userId(), page));
    }

    @Operation(
            summary = "목표 삭제",
            description = "지정한 목표를 삭제합니다."
    )
    @DeleteMapping("/delete/{goalId}")
    public ResponseEntity<ApiResponse<Object>> deleteGoal(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long goalId
    ) {
        goalService.deleteGoal(userPrincipal.userId(), goalId);

        return ApiResponse.success(SuccessCode.GOAL_DELETE);
    }

    //과제
    @Operation(
            summary = "과제 생성",
            description = "지정한 목표에 과제를 생성합니다."
    )
    @PostMapping("/create/{goalId}/task")
    public ResponseEntity<ApiResponse<CreateTaskResponseDto>> createTask(
            @Valid @RequestBody CreateTaskRequestDto req,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long goalId
    ) {
        CreateTaskResponseDto res = goalService.createTask(userPrincipal.userId(), goalId, req);

        return ApiResponse.success(SuccessCode.TASK_CREATED, res);
    }

    @Operation(
            summary = "과제 목록 조회",
            description = "지정한 목표의 과제 목록을 조회합니다."
    )
    @GetMapping("/lists/{goalId}/task")
    public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long goalId
    ) {
        return ApiResponse.success(SuccessCode.TASK_LIST_SUCCESS, goalService.getTasks(userPrincipal.userId(), goalId));
    }

    @Operation(
            summary = "과제 체크 상태 변경",
            description = "지정한 과제의 체크 상태를 변경합니다."
    )
    @PatchMapping("/{goalId}/task/{taskId}/checkbox")
    public ResponseEntity<ApiResponse<Object>> checkTask(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long goalId,
            @PathVariable Long taskId
    ) {
        goalService.checkTask(userPrincipal.userId(), goalId, taskId);

        return ApiResponse.success(SuccessCode.TASK_CHECKBOX_UPDATE);
    }

    @Operation(
            summary = "과제 삭제",
            description = "지정한 과제를 삭제합니다."
    )
    @DeleteMapping("/delete/{goalId}/task/{taskId}")
    public ResponseEntity<ApiResponse<Object>> deleteTask(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long goalId,
            @PathVariable Long taskId
    ) {
        goalService.deleteTask(userPrincipal.userId(), goalId, taskId);

        return ApiResponse.success(SuccessCode.TASK_DELETE);
    }
}

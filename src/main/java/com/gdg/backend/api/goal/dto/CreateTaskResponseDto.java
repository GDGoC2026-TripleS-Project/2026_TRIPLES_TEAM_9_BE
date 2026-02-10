package com.gdg.backend.api.goal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTaskResponseDto {

    private Long taskId;
}

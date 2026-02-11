package com.gdg.backend.api.goal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateGoalRequestDto {

    @NotBlank(message = "title 입력은 필수입니다.")
    @Size(max = 500, message = "title은 최대 500자입니다.")
    private String title;
}

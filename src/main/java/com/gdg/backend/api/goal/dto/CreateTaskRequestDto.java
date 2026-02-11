package com.gdg.backend.api.goal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTaskRequestDto {

    @NotBlank(message = "content 입력은 필수입니다.")
    @Size(max = 5000, message = "content는 최대 5,000자입니다.")
    private String content;
}

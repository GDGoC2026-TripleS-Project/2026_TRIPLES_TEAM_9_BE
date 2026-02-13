package com.gdg.backend.api.record.dto;

import com.gdg.backend.api.record.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class UpdateRecordDetailRequestDto {

    @NotNull
    private LocalDate learningDate;

    @NotNull
    private Category category;

    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 20000, message = "content는 최대 20,000자입니다.")
    private String content;

    @NotEmpty
    private List<String> keywords;
}

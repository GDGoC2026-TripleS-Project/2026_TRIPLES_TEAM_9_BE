package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private String content;

    @NotEmpty
    private List<String> keywords;
}

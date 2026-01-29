package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CreateRecordRequestDto {

    //학습 날짜
    @NotNull(message = "learningDate 입력은 필수입니다.")
    private LocalDate learningDate;

    //카테고리
    @NotNull(message = "category 선택은 필수입니다.")
    private Category category;

    //제목
    @NotBlank(message = "title 입력은 필수입니다.")
    @Size(max = 200, message = "title은 최대 200자입니다.")
    private String title;

    //내용
    @NotBlank(message = "content 입력은 필수입니다.")
    @Size(max = 1000, message = "content는 최대 1000자입니다.")
    private String content;

    //키워드
    @NotEmpty(message = "keyword 입력은 필수입니다.")
    private List<String> keywords;
}

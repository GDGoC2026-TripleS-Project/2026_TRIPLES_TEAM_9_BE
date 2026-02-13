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
public class CreateRecordRequestDto {

    //학습 날짜
    @NotNull(message = "learningDate 입력은 필수입니다.")
    private LocalDate learningDate;

    //카테고리
    @NotNull(message = "category 선택은 필수입니다.")
    private Category category;

    //제목
    @NotBlank(message = "title 입력은 필수입니다.")
    @Size(max = 500, message = "title은 최대 500자입니다.")
    private String title;

    //내용
    @NotBlank(message = "content 입력은 필수입니다.")
    @Size(max = 20000, message = "content는 최대 20,000자입니다.")
    private String content;

    //키워드 리스트
    @NotEmpty(message = "keyword 입력은 필수입니다.")
    @Size(max = 5, message = "keyword는 최대 5개까지 입력할 수 있습니다.")
    private List<
            @NotBlank(message = "keyword엔 빈 값이 들어갈 수 없습니다.")
            @Size(max = 50, message = "keyword는 최대 50자입니다.")
            String
            > keywords;
}

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
    @NotNull(message = "학습날짜를 입력해주세요.")
    private LocalDate learningDate;

    //카테고리
    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    //제목
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 500, message = "제목은 최대 500자입니다.")
    private String title;

    //내용
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 20000, message = "내용은 최대 20,000자입니다.")
    private String content;

    //키워드 리스트
    @NotEmpty(message = "키워드를 입력해주세요.")
    @Size(max = 5, message = "키워드는 최대 5개까지 입력할 수 있습니다.")
    private List<
            @NotBlank(message = "키워드에 빈 값은 입력할 수 없습니다.")
            @Size(max = 50, message = "키워드는 최대 50자입니다.")
            String
            > keywords;
}

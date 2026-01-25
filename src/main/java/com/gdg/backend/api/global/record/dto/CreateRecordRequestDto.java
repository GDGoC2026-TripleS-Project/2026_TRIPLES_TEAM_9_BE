package com.gdg.backend.api.global.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateRecordRequestDto {

//    //학습기록 생성시간(서버에서 해당일 23:59:59로 저장)
//    @NotNull(message = "groupEndAt은 필수입니다.")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private LocalDate groupEndAt;

    //카테고리

    //제목
    @NotBlank(message = "title 입력은 필수입니다.")
    @Size(max = 200, message = "title은 최대 200자입니다.")
    private String title;

    //내용
    @NotBlank(message = "content 입력은 필수입니다.")
    @Size(max = 1000, message = "content는 최대 1000자입니다.")
    private String content;

    //키워드
    @NotBlank(message = "keyword 입력은 필수입니다.")
    @Size(max = 200, message = "keyword는 최대 200자입니다.")
    private String keyword;
}

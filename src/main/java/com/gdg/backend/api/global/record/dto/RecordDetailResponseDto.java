package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecordDetailResponseDto {

    private Long recordId;
    private LocalDate learningDate;
    private Category category;
    private String title;
    private String content;
    private List<String> keywords;

    public static RecordDetailResponseDto from(Record record) {
        return new RecordDetailResponseDto(
                record.getId(),
                record.getLearningDate(),
                record.getCategory(),
                record.getTitle(),
                record.getContent(),
                record.getKeywords()
        );
    }
}

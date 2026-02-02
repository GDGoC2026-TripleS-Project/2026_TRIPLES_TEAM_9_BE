package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Record;
import com.gdg.backend.api.global.record.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateRecordDetailResponseDto {

    private Long recordId;
    private LocalDate learningDate;
    private Category category;
    private String title;
    private String content;
    private List<String> keywords;

    public static UpdateRecordDetailResponseDto from(Record record) {
        return new UpdateRecordDetailResponseDto(
                record.getId(),
                record.getLearningDate(),
                record.getCategory(),
                record.getTitle(),
                record.getContent(),
                record.getKeywords()
        );
    }
}

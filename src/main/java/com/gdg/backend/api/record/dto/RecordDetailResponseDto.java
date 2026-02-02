package com.gdg.backend.api.record.dto;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecordDetailResponseDto {

    private Long recordId;
    private LocalDate learningDate;
    private Category category;
    private String title;
    private String content;
    private List<String> keywords;

    public static RecordDetailResponseDto from(Record record) {
        return RecordDetailResponseDto.builder()
                .recordId(record.getId())
                .learningDate(record.getLearningDate())
                .category(record.getCategory())
                .title(record.getTitle())
                .content(record.getContent())
                .keywords(record.getKeywords())
                .build();
    }
}

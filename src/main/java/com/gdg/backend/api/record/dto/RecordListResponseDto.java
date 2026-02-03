package com.gdg.backend.api.record.dto;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class RecordListResponseDto {

    private Long recordId;
    private LocalDate learningDate;
    private Category category;
    private String title;
    private String contentPreview;
    private List<String> keywords;

    public static RecordListResponseDto from(Record record) {
        return new RecordListResponseDto(
                record.getId(),
                record.getLearningDate(),
                record.getCategory(),
                record.getTitle(),
                extractFirstLine(record.getContent()),
                record.getKeywordNames()
        );
    }

    private static String extractFirstLine(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        return content.split("\\R", 2)[0];
    }
}

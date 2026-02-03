package com.gdg.backend.api.dashBoard.dto;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashBoardRecentActivityDto {
    private Long recordId;
    private LocalDate learningDate;
    private Category category;
    private String title;
    private List<String> keywords;

    public static DashBoardRecentActivityDto from(Record record) {
        return new DashBoardRecentActivityDto(
                record.getId(),
                record.getLearningDate(),
                record.getCategory(),
                record.getTitle(),
                record.getKeywordNames()
        );
    }
}

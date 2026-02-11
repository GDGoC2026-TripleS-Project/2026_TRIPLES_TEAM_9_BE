package com.gdg.backend.api.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class MindmapRecordItemDto {
    private Long recordId;
    private String title;
    private LocalDate learningDate;
    private String preview;
    private List<String> keywords;
}

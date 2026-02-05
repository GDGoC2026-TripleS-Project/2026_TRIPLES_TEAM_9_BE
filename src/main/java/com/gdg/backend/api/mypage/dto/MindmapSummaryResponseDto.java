package com.gdg.backend.api.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MindmapSummaryResponseDto {
    private List<MindmapKeywordItemDto> keywords;
    private MindmapKeywordItemDto selected;
    private List<MindmapRecordItemDto> records;
}

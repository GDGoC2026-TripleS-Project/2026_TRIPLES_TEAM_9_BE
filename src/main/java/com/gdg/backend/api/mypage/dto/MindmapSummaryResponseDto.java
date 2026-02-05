package com.gdg.backend.api.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MindmapSummaryResponseDto {
    private List<MindmapKeywordItemDto> keywords;
    private MindmapKeywordItemDto selected;
    private List<MindmapRecordItemDto> records;

    @Builder
    public MindmapSummaryResponseDto(
            List<MindmapKeywordItemDto> keywords,
            MindmapKeywordItemDto selected,
            List<MindmapRecordItemDto> records
    ){
        this.keywords = keywords;
        this.selected = selected;
        this.records = records;
    }
}

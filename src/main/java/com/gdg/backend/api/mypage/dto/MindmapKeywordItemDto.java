package com.gdg.backend.api.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MindmapKeywordItemDto {
    private Long keywordId;
    private String name;
    private long count;
}

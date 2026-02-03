package com.gdg.backend.api.mindMap.dto;

public record MindMapNodeResponseDto(
        Long id,
        Long keywordId, //keyword FK, category로 변경시 이름 변경
        String title,
        String category,
        int x,
        int y,
        long recordCount
) {
}

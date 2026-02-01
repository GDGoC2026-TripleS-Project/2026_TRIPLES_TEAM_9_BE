package com.gdg.backend.api.mindMap.dto;

import com.gdg.backend.api.mindMap.domain.MindMapScope;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MindMapResponseDto(
        Long id,
        String title,
        MindMapScope scope,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<MindMapNodeResponseDto> nodes,
        List<MindMapEdgeResponseDto> edges
) {
}

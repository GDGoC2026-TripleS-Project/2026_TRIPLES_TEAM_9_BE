package com.gdg.backend.api.mindMap.dto;

public record MindMapEdgeResponseDto(
        Long id,
        Long sourceNodeId,
        Long targetNodeId,
        int weight
) {
}

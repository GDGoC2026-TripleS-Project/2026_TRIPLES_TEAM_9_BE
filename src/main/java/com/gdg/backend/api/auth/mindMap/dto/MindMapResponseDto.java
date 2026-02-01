package com.gdg.backend.api.auth.mindMap.dto;

import com.gdg.backend.api.auth.mindMap.domain.MindMapEdge;
import com.gdg.backend.api.auth.mindMap.domain.MindMapNode;
import com.gdg.backend.api.auth.mindMap.domain.MindMapScope;

import java.time.LocalDateTime;
import java.util.List;

public record MindMapResponseDto(
        Long id,
        String title,
        MindMapScope scope,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        List<MindMapNode> nodes,
        List<MindMapEdge> edges
) {
}

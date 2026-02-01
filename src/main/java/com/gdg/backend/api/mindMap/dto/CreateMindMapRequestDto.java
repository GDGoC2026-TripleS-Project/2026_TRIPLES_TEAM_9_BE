package com.gdg.backend.api.mindMap.dto;

import com.gdg.backend.api.mindMap.domain.MindMapScope;

public record CreateMindMapRequestDto(
        String title,
        MindMapScope scope
) {
}

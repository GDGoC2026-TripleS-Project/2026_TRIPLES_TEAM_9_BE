package com.gdg.backend.api.mindMap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MindMapResponseDto {
    private final List<MindMapNodeDto> nodes;
    private final List<MindMapEdgeDto> edges;
}

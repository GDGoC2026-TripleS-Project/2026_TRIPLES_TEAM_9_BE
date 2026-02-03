package com.gdg.backend.api.mindMap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MindMapResponseDto {
    private List<MindMapNodeDto> nodes;
    private List<MindMapEdgeDto> edges;
}

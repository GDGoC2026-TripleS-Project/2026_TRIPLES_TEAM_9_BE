package com.gdg.backend.api.mindMap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MindMapEdgeDto {
    private Long source;
    private Long target;
    private long weight;
}

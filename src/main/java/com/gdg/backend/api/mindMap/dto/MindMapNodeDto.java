package com.gdg.backend.api.mindMap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MindMapNodeDto {
    private Long id;
    private String label;
    private long weight;
}

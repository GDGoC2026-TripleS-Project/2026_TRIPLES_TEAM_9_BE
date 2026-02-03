package com.gdg.backend.api.mindMap.dto;

import java.util.List;

public record UpdateNodePositionsRequestDto(List<NodePositionDto> positions) {
    public record NodePositionDto(
            Long nodeId,
            int x,
            int y
    ) {}
}

package com.gdg.backend.api.mindMap.service;

import com.gdg.backend.api.mindMap.dto.MindMapEdgeDto;
import com.gdg.backend.api.mindMap.dto.MindMapNodeDto;
import com.gdg.backend.api.mindMap.dto.MindMapResponseDto;
import com.gdg.backend.api.mindMap.repository.MindMapRepository;
import com.gdg.backend.api.record.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MindMapService {

    private final MindMapRepository mindMapRepository;

    public MindMapResponseDto getMindMap(
            Long userId,
            LocalDate from,
            LocalDate to,
            Category category,
            int topKeywords,
            int minEdgeWeight
    ) {
        if (topKeywords <= 0) {
            return MindMapResponseDto.builder()
                    .nodes(Collections.emptyList())
                    .edges(Collections.emptyList())
                    .build();
        }

        String categoryValue = category == null ? null : category.name();

        List<MindMapRepository.NodeProjection> nodeRows = mindMapRepository.findTopKeywords(
                userId,
                from,
                to,
                categoryValue,
                PageRequest.of(0, topKeywords)
        );

        List<MindMapNodeDto> nodes = nodeRows.stream()
                .map(row -> MindMapNodeDto.builder()
                        .id(row.getId())
                        .label(row.getLabel())
                        .weight(row.getWeight() == null ? 0L : row.getWeight())
                        .build())
                .toList();

        if (nodes.isEmpty()) {
            return MindMapResponseDto.builder()
                    .nodes(nodes)
                    .edges(Collections.emptyList())
                    .build();
        }

        List<Long> keywordIds = nodes.stream()
                .map(MindMapNodeDto::getId)
                .toList();

        List<MindMapEdgeDto> edges = mindMapRepository.findEdges(
                        userId,
                        from,
                        to,
                        categoryValue,
                        keywordIds,
                        minEdgeWeight
                )
                .stream()
                .map(row -> MindMapEdgeDto.builder()
                        .source(row.getSource())
                        .target(row.getTarget())
                        .weight(row.getWeight() == null ? 0L : row.getWeight())
                        .build())
                .toList();

        return MindMapResponseDto.builder()
                .nodes(nodes)
                .edges(edges)
                .build();
    }
}

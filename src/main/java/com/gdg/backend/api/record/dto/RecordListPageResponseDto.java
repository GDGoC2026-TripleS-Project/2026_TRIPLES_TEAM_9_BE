package com.gdg.backend.api.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecordListPageResponseDto {

    private final List<RecordListResponseDto> items;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;
    private final boolean hasNext;
    private final boolean hasPrev;
}

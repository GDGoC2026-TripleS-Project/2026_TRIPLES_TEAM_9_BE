package com.gdg.backend.api.dashBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashBoardSummaryDto {
    private long totalRecords;
    private long totalKeywords;
    private long totalCategories;
}

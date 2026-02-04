package com.gdg.backend.api.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DashboardSummaryDto {
    private long totalRecords;
    private long totalKeywords;
    private long totalCategories;
}

package com.gdg.backend.api.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {
    private DashboardSummaryDto summary;
    private List<DashboardCategoryStatDto> categoryStats;
    private List<DashboardRecentActivityDto> recentActivities;
}

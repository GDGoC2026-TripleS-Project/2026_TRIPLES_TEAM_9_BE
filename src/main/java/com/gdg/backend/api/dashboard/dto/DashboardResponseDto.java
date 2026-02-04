package com.gdg.backend.api.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DashboardResponseDto {
    private DashboardSummaryDto summary;
    private List<DashboardCategoryStatDto> categoryStats;
    private List<DashboardRecentActivityDto> recentActivities;

    public static DashboardResponseDto from(
            DashboardSummaryDto summary,
            List<DashboardCategoryStatDto> categoryStats,
            List<DashboardRecentActivityDto> recentActivities
    ){
        return DashboardResponseDto.builder()
                .summary(summary)
                .categoryStats(categoryStats)
                .recentActivities(recentActivities)
                .build();
    }
}

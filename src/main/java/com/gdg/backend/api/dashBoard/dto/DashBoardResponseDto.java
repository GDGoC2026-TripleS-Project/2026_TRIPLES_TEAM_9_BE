package com.gdg.backend.api.dashBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashBoardResponseDto {
    private DashBoardSummaryDto summary;
    private List<DashBoardCategoryStatDto> categoryStats;
    private List<DashBoardRecentActivityDto> recentActivities;
}

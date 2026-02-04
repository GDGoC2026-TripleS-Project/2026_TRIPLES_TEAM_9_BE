package com.gdg.backend.api.dashboard.dto;

import com.gdg.backend.api.record.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardCategoryStatDto {
    private Category category;
    private Long count;
}

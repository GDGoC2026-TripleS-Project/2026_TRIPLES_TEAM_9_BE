package com.gdg.backend.api.dashboard.dto;

public record DashboardMonthCountRawDto(
        Integer year,
        Integer month,
        Long count
) {
}

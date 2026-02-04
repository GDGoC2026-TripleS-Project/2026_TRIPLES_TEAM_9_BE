package com.gdg.backend.api.dashboard.dto;

public record DashboardMonthCountResponseDto(
        String month,
        long count
) {}

package com.gdg.backend.api.dashboard.service;

import com.gdg.backend.api.dashboard.dto.DashboardCategoryStatDto;
import com.gdg.backend.api.dashboard.dto.DashboardMonthCountRawDto;
import com.gdg.backend.api.dashboard.dto.DashboardMonthCountResponseDto;
import com.gdg.backend.api.dashboard.dto.DashboardRecentActivityDto;
import com.gdg.backend.api.dashboard.dto.DashboardRequestDto;
import com.gdg.backend.api.dashboard.dto.DashboardResponseDto;
import com.gdg.backend.api.dashboard.dto.DashboardSummaryDto;
import com.gdg.backend.api.dashboard.repository.DashboardRepository;
import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboard(Long userId, DashboardRequestDto req) {
        LocalDate from = req.getFrom();
        LocalDate to = req.getTo();
        Category category = req.getCategory();
        int recentLimit = req.getRecentLimitOrDefault();

        long totalRecords = dashboardRepository.countRecords(userId, from, to, category);
        long totalKeywords = dashboardRepository.countDistinctKeywords(userId, from, to, category);
        long totalCategories = dashboardRepository.countDistinctCategories(userId, from, to, category);

        List<DashboardCategoryStatDto> categoryStats =
                dashboardRepository.categoryStats(userId, from, to, category);

        List<Record> recentRecords = dashboardRepository.findRecentWithKeywords(
                userId,
                from,
                to,
                category,
                PageRequest.of(0, recentLimit)
        );

        List<DashboardRecentActivityDto> recentActivities = recentRecords.stream()
                .map(DashboardRecentActivityDto::from)
                .toList();

        DashboardSummaryDto summaryDto = DashboardSummaryDto.builder()
                .totalRecords(totalRecords)
                .totalKeywords(totalKeywords)
                .totalCategories(totalCategories)
                .build();

        return DashboardResponseDto.from(summaryDto, categoryStats, recentActivities);
    }

    public List<DashboardMonthCountResponseDto> getMonthlyStats(Long userId, LocalDate from, LocalDate to) {
        List<DashboardMonthCountRawDto> raw = dashboardRepository.findMonthlyCounts(userId, from, to);
        return raw.stream()
                .map(item -> new DashboardMonthCountResponseDto(
                        String.format("%d.%02d", item.year(), item.month()),
                        item.count()
                ))
                .toList();
    }
}

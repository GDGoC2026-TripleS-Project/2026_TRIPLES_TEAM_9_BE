package com.gdg.backend.api.dashBoard.service;

import com.gdg.backend.api.dashBoard.dto.DashBoardCategoryStatDto;
import com.gdg.backend.api.dashBoard.dto.DashBoardRecentActivityDto;
import com.gdg.backend.api.dashBoard.dto.DashBoardRequestDto;
import com.gdg.backend.api.dashBoard.dto.DashBoardResponseDto;
import com.gdg.backend.api.dashBoard.dto.DashBoardSummaryDto;
import com.gdg.backend.api.dashBoard.repository.DashBoardRepository;
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
public class DashBoardService {

    private final DashBoardRepository dashBoardRepository;

    @Transactional(readOnly = true)
    public DashBoardResponseDto getDashBoard(Long userId, DashBoardRequestDto req) {
        LocalDate from = req.getFrom();
        LocalDate to = req.getTo();
        Category category = req.getCategory();
        int recentLimit = req.getRecentLimitOrDefault();

        long totalRecords = dashBoardRepository.countRecords(userId, from, to, category);
        long totalKeywords = dashBoardRepository.countDistinctKeywords(userId, from, to, category);
        long totalCategories = dashBoardRepository.countDistinctCategories(userId, from, to, category);

        List<DashBoardCategoryStatDto> categoryStats =
                dashBoardRepository.categoryStats(userId, from, to, category);

        List<Record> recentRecords = dashBoardRepository.findRecentWithKeywords(
                userId,
                from,
                to,
                category,
                PageRequest.of(0, recentLimit)
        );

        List<DashBoardRecentActivityDto> recentActivities = recentRecords.stream()
                .map(DashBoardRecentActivityDto::from)
                .toList();

        DashBoardSummaryDto summaryDto = new DashBoardSummaryDto(
                totalRecords,
                totalKeywords,
                totalCategories
        );

        return new DashBoardResponseDto(summaryDto, categoryStats, recentActivities);
    }
}

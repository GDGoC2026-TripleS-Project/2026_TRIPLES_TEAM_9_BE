package com.gdg.backend.api.mypage.service;

import com.gdg.backend.api.mypage.dto.MindmapKeywordItemDto;
import com.gdg.backend.api.mypage.dto.MindmapRecordItemDto;
import com.gdg.backend.api.mypage.dto.MindmapSummaryResponseDto;
import com.gdg.backend.api.mypage.repository.MindmapSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MindmapSummaryService {

    private static final int DEFAULT_TOP = 5;
    private static final int MAX_TOP = 20;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;
    private static final int PREVIEW_MAX = 100;

    private final MindmapSummaryRepository mindmapSummaryRepository;

    @Transactional(readOnly = true)
    public MindmapSummaryResponseDto getSummary(Long userId, Integer top, Long keywordId, Integer size) {
        int topLimit = clamp(top, DEFAULT_TOP, MAX_TOP);
        int sizeLimit = clamp(size, DEFAULT_SIZE, MAX_SIZE);

        List<MindmapKeywordItemDto> keywordCounts =
                mindmapSummaryRepository.findKeywordCountsByUserId(userId);

        if (keywordCounts.isEmpty()) {
            return new MindmapSummaryResponseDto(List.of(), null, List.of());
        }

        List<MindmapKeywordItemDto> keywords = keywordCounts.subList(
                0,
                Math.min(topLimit, keywordCounts.size())
        );

        MindmapKeywordItemDto selected = null;
        if (keywordId != null) {
            for (MindmapKeywordItemDto item : keywordCounts) {
                if (item.getKeywordId().equals(keywordId)) {
                    selected = item;
                    break;
                }
            }
        }

        if (selected == null) {
            selected = keywordCounts.get(0);
        }

        List<Object[]> recordRows = mindmapSummaryRepository.findRecentRecordsByUserIdAndKeywordId(
                userId,
                selected.getKeywordId(),
                PageRequest.of(0, sizeLimit)
        );

        if (recordRows.isEmpty()) {
            return new MindmapSummaryResponseDto(keywords, selected, List.of());
        }

        List<Long> recordIds = new ArrayList<>(recordRows.size());
        for (Object[] row : recordRows) {
            recordIds.add((Long) row[0]);
        }

        Map<Long, List<String>> recordKeywords = new HashMap<>();
        for (Object[] row : mindmapSummaryRepository.findKeywordNamesByRecordIds(recordIds)) {
            Long recordId = (Long) row[0];
            String name = (String) row[1];
            recordKeywords.computeIfAbsent(recordId, key -> new ArrayList<>()).add(name);
        }

        List<MindmapRecordItemDto> records = new ArrayList<>(recordRows.size());
        for (Object[] row : recordRows) {
            Long recordId = (Long) row[0];
            String title = (String) row[1];
            LocalDate learningDate = (LocalDate) row[2];
            String content = (String) row[3];

            records.add(new MindmapRecordItemDto(
                    recordId,
                    title,
                    learningDate,
                    buildPreview(content),
                    recordKeywords.getOrDefault(recordId, List.of())
            ));
        }

        // Verification: enable hibernate show_sql and confirm 3 queries (keywords, records, tags)
        return new MindmapSummaryResponseDto(keywords, selected, records);
    }

    private int clamp(Integer value, int defaultValue, int max) {
        if (value == null || value <= 0) {
            return defaultValue;
        }
        return Math.min(value, max);
    }

    private String buildPreview(String content) {
        if (content == null) {
            return "";
        }
        String normalized = content.replaceAll("\\s+", " ").trim();
        if (normalized.isEmpty()) {
            return "";
        }
        if (normalized.length() <= PREVIEW_MAX) {
            return normalized;
        }
        return normalized.substring(0, PREVIEW_MAX) + "...";
    }
}

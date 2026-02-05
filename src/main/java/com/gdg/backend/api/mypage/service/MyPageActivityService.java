package com.gdg.backend.api.mypage.service;

import com.gdg.backend.api.mypage.dto.RecentActivityDto;
import com.gdg.backend.api.record.domain.Record;
import com.gdg.backend.api.record.repository.RecordKeywordRepository;
import com.gdg.backend.api.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPageActivityService {

    private final RecordRepository recordRepository;
    private final RecordKeywordRepository recordKeywordRepository;

    public List<RecentActivityDto> getRecentActivities(Long userId, int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);

        List<Record> records = recordRepository.findRecentByUserId(
                userId,
                PageRequest.of(0, safeSize)
        );

        List<Long> recordIds = records.stream().map(Record::getId).toList();

        Map<Long, List<String>> keywordsMap = new HashMap<>();
        if (!recordIds.isEmpty()) {
            for (Object[] row : recordKeywordRepository.findKeywordNamesByRecordIds(recordIds)) {
                Long recordId = (Long) row[0];
                String keywordName = (String) row[1];

                keywordsMap.computeIfAbsent(recordId, k -> new ArrayList<>()).add(keywordName);
            }
        }

        return records.stream()
                .map(r -> new RecentActivityDto(
                        r.getId(),
                        r.getTitle(),
                        r.getCategory(),
                        r.getLearningDate(),
                        makePreview(r.getContent()),
                        limitTags(keywordsMap.getOrDefault(r.getId(), List.of()), 3) // UI용 태그 제한(선택)
                ))
                .toList();
    }

    private String makePreview(String content) {
        if (content == null) return "";
        String plain = content.replaceAll("\\s+", " ").trim();
        return plain.length() > 90 ? plain.substring(0, 90) + "..." : plain;
    }

    private List<String> limitTags(List<String> tags, int max) {
        if (tags.size() <= max) return tags;
        return tags.subList(0, max);
    }
}

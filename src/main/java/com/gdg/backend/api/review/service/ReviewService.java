package com.gdg.backend.api.review.service;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import com.gdg.backend.api.record.repository.RecordRepository;
import com.gdg.backend.api.review.domain.UserReviewLog;
import com.gdg.backend.api.review.dto.ReviewTodayResponseDto;
import com.gdg.backend.api.review.repository.UserReviewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RecordRepository recordRepository;
    private final UserReviewLogRepository userReviewLogRepository;

    private static final int[] OFFSETS = {1, 3, 7, 14, 30};

    @Transactional(readOnly = true)
    public ReviewTodayResponseDto getTodayReview(Long userId) {
        LocalDate today = LocalDate.now();

        List<LocalDate> dates = new ArrayList<>();
        for (int d : OFFSETS) {
            dates.add(today.minusDays(d));
        }

        List<Long> viewedIds = userReviewLogRepository.findViewedRecordIds(userId, today);
        boolean excludeIdsEmpty = viewedIds == null || viewedIds.isEmpty();
        if (excludeIdsEmpty) {
            viewedIds = List.of(0L);
        }

        List<Record> candidates = recordRepository.findReviewCandidatesExcludeIds(
                userId,
                dates,
                viewedIds,
                excludeIdsEmpty
        );

        if (candidates.isEmpty()) {
            return ReviewTodayResponseDto.builder()
                    .shouldShow(false)
                    .items(List.of())
                    .build();
        }

        List<ReviewTodayResponseDto.Item> items = candidates.stream()
                .limit(3)
                .map(record -> ReviewTodayResponseDto.Item.builder()
                        .recordId(record.getId())
                        .title(record.getTitle())
                        .categoryLabel(toCategoryLabel(record.getCategory()))
                        .learningDate(record.getLearningDate())
                        .preview(makePreview(record.getContent()))
                        .keywords(record.getKeywords().stream()
                                .map(keyword -> keyword.getName())
                                .limit(3)
                                .toList())
                        .build())
                .toList();

        return ReviewTodayResponseDto.builder()
                .shouldShow(!items.isEmpty())
                .items(items)
                .build();
    }

    @Transactional
    public void markViewedBatch(Long userId, List<Long> recordIds) {
        if (recordIds == null || recordIds.isEmpty()) {
            return;
        }

        LocalDate today = LocalDate.now();
        List<Long> distinctIds = recordIds.stream()
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();
        if (distinctIds.isEmpty()) {
            return;
        }

        List<Long> already = userReviewLogRepository.findAlreadyViewedIn(userId, today, distinctIds);
        Set<Long> alreadySet = already == null ? Set.of() : already.stream().collect(Collectors.toSet());

        List<Long> toSave = distinctIds.stream()
                .filter(id -> !alreadySet.contains(id))
                .toList();

        if (toSave.isEmpty()) {
            return;
        }

        List<UserReviewLog> logs = toSave.stream()
                .map(id -> UserReviewLog.of(userId, id, today))
                .toList();

        userReviewLogRepository.saveAll(logs);
    }

    private String makePreview(String content) {
        if (content == null || content.isBlank()) return "";
        String oneLine = content.replace("\n", " ").replace("\r", " ").trim();
        int max = 120;
        if (oneLine.length() <= max) return oneLine;
        return oneLine.substring(0, max) + "...";
    }

    private String toCategoryLabel(Category category) {
        if (category == null) return "개인학습";
        return switch (category) {
            case LECTURE -> "강의";
            case READING -> "독서";
            case PROJECT -> "프로젝트";
            case SEMINAR -> "세미나";
            case PERSONAL -> "개인학습";
            case OTHER -> "기타";
        };
    }
}

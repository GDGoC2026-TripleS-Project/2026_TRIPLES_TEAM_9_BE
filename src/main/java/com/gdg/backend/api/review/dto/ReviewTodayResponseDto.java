package com.gdg.backend.api.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ReviewTodayResponseDto {

    private final boolean shouldShow;
    private final List<Item> items;

    @Getter
    @Builder
    public static class Item {
        private final Long recordId;
        private final String title;
        private final String categoryLabel;
        private final LocalDate learningDate;
        private final String preview;
        private final List<String> keywords;
    }
}

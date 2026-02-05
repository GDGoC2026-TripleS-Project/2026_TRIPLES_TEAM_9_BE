package com.gdg.backend.api.mypage.dto;

import com.gdg.backend.api.record.domain.Category;

import java.time.LocalDate;
import java.util.List;

public record RecentActivityDto(
        Long recordId,
        String title,
        Category category,
        LocalDate learningDate,
        String preview,
        List<String> keywords
) {}

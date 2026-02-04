package com.gdg.backend.api.dashboard.dto;

import com.gdg.backend.api.record.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DashboardRequestDto {
    private static final int DEFAULT_RECENT_LIMIT = 5;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private Category category;

    private Integer recentLimit;

    public int getRecentLimitOrDefault() {
        if (recentLimit == null || recentLimit <= 0) {
            return DEFAULT_RECENT_LIMIT;
        }
        return recentLimit;
    }
}

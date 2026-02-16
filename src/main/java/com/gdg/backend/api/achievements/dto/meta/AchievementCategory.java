package com.gdg.backend.api.achievements.dto.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AchievementCategory {
    LEARNING("LEARNING", "학습"),
    ATTENDANCE("ATTENDANCE", "출석"),
    CHALLENGE("CHALLENGE", "도전");

    private final String code;
    private final String title;
}

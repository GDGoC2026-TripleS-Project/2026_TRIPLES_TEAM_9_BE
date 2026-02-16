package com.gdg.backend.api.achievements.dto;

import java.util.List;

public record AchievementSection(
        String category,
        String title,
        List<BadgeItem> badges
) {
    public static AchievementSection of(AchievementCategory category, List<BadgeItem> badges) {
        return new AchievementSection(category.getCode(), category.getTitle(), badges);
    }
}
